package com.cardpaymentsystem.paymentservice.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cardpaymentsystem.paymentservice.controller.dto.ApprovalRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.ApprovalResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentSearchDto;
import com.cardpaymentsystem.paymentservice.controller.dto.TokenRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.TokenResponseDto;
import com.cardpaymentsystem.paymentservice.entity.Payment;
import com.cardpaymentsystem.paymentservice.entity.mapper.PaymentMapper;
import com.cardpaymentsystem.paymentservice.exception.NotFoundException;
import com.cardpaymentsystem.paymentservice.exception.ValidationException;
import com.cardpaymentsystem.paymentservice.repository.PaymentRepository;
import com.cardpaymentsystem.paymentservice.service.ApiService;
import com.cardpaymentsystem.paymentservice.service.PaymentService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final ApiService apiService;
	private final PaymentRepository paymentRepository;
	private static final String FORMAT_YYYYMMDD = "yyyyMMdd";
	private static final String CREATED_DATE = "createdDate";
	private static final String CARD_REF_ID = "cardRefId";
	private static final String PAYMENT_AMOUNT = "paymentAmount";
	private static final String STORE_ID = "storeId";
	private static final String IS_APPROVED = "isApproved";
	private static final String APPROVAL_ID = "approvalId";
	private static final String APPROVED_DATE = "approvedDate";

	public PaymentResponseDto requestPayment(PaymentRequestDto paymentRequestDto) throws Exception {

		validateRequestPayment(paymentRequestDto);

		String tokenIv = generateTokenIv();
		TokenRequestDto tokenRequestDto = TokenRequestDto.builder()
			.cardRefId(paymentRequestDto.getCardRefId())
			.userCi(paymentRequestDto.getUserCi())
			.tokenIv(tokenIv)
			.build();
		TokenResponseDto tokenResponseDto = apiService.createToken(tokenRequestDto);
		String token = tokenResponseDto.getToken();

		Payment payment = Payment.builder()
			.cardRefId(paymentRequestDto.getCardRefId())
			.paymentAmount(paymentRequestDto.getPaymentAmount())
			.storeId(paymentRequestDto.getStoreId())
			.isApproved(false)
			.build();
		Long paymentId = paymentRepository.save(payment).getPaymentId();

		ApprovalRequestDto approvalRequestDto = ApprovalRequestDto.builder()
			.token(token)
			.tokenIv(tokenIv)
			.userCi(paymentRequestDto.getUserCi())
			.paymentAmount(paymentRequestDto.getPaymentAmount())
			.storeId(paymentRequestDto.getStoreId())
			.build();
		ApprovalResponseDto approvalResponseDto = apiService.requestApproval(approvalRequestDto);

		String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
		apiService.deleteToken(encodedToken);

		payment.setIsApproved(true);
		payment.setApprovalId(approvalResponseDto.getApprovalId());
		payment.setApprovedDate(LocalDateTime.now());
		paymentRepository.save(payment);

		return PaymentResponseDto.builder()
			.paymentId(paymentId)
			.build();
	}

	public List<PaymentDto> searchPayment(PaymentSearchDto paymentSearchDto) throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YYYYMMDD);

		List<Payment> paymentList = paymentRepository.findAll((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.between(root.get(CREATED_DATE),
				LocalDate.parse(paymentSearchDto.getStartDate(), formatter).atStartOfDay(),
				LocalDate.parse(paymentSearchDto.getEndDate(), formatter).atTime(23, 59, 59)));

			if (paymentSearchDto.getCardRefId() != null && !paymentSearchDto.getCardRefId().isBlank()) {
				predicates.add(builder.equal(root.get(CARD_REF_ID), paymentSearchDto.getCardRefId()));
			}

			if (paymentSearchDto.getStartAmount() != null
				&& paymentSearchDto.getStartAmount().compareTo(BigDecimal.ZERO) >= 0) {
				predicates.add(
					builder.greaterThanOrEqualTo(root.get(PAYMENT_AMOUNT), paymentSearchDto.getStartAmount()));
			}

			if (paymentSearchDto.getEndAmount() != null
				&& paymentSearchDto.getEndAmount().compareTo(BigDecimal.ZERO) >= 0) {
				predicates.add(
					builder.lessThanOrEqualTo(root.get(PAYMENT_AMOUNT), paymentSearchDto.getEndAmount()));
			}

			if (paymentSearchDto.getStoreId() != null && !paymentSearchDto.getStoreId().isBlank()) {
				predicates.add(builder.equal(root.get(STORE_ID), paymentSearchDto.getStoreId()));
			}

			if (paymentSearchDto.getIsApproved() != null) {
				predicates.add(builder.equal(root.get(IS_APPROVED), paymentSearchDto.getIsApproved()));
			}

			if (paymentSearchDto.getApprovalId() != null && paymentSearchDto.getApprovalId() > 0) {
				predicates.add(builder.equal(root.get(APPROVAL_ID), paymentSearchDto.getApprovalId()));
			}

			if (paymentSearchDto.getApprovedStartDate() != null && !paymentSearchDto.getApprovedStartDate().isBlank()) {
				predicates.add(
					builder.greaterThanOrEqualTo(root.get(APPROVED_DATE),
						LocalDate.parse(paymentSearchDto.getApprovedStartDate(), formatter).atStartOfDay()));
			}

			if (paymentSearchDto.getApprovedEndDate() != null && !paymentSearchDto.getApprovedEndDate().isBlank()) {
				predicates.add(
					builder.lessThanOrEqualTo(root.get(APPROVED_DATE),
						LocalDate.parse(paymentSearchDto.getApprovedEndDate(), formatter).atTime(23, 59, 59)));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		});

		if (paymentList.isEmpty()) {
			throw new NotFoundException("No results found");
		}

		return paymentList.stream()
			.map(PaymentMapper.INSTANCE::toDto)
			.collect(Collectors.toList());
	}

	private void validateRequestPayment(PaymentRequestDto paymentRequestDto) throws Exception {

		String cardRefId = paymentRequestDto.getCardRefId();
		if (cardRefId == null || cardRefId.isBlank()) {
			throw new ValidationException("No card reference ID");
		}
		String userCi = paymentRequestDto.getUserCi();
		if (userCi == null || userCi.isBlank()) {
			throw new ValidationException("No user CI");
		}
		BigDecimal paymentAmount = paymentRequestDto.getPaymentAmount();
		if (paymentAmount == null || paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("No payment amount");
		}
		String storeId = paymentRequestDto.getStoreId();
		if (storeId == null || storeId.isBlank()) {
			throw new ValidationException("No store ID");
		}
	}

	private String generateTokenIv() {

		byte[] iv = new byte[16];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(iv);

		return Base64.getEncoder().encodeToString(iv);
	}
}
