package com.cardpaymentsystem.paymentservice.service.impl;

import java.time.YearMonth;

import org.springframework.stereotype.Service;

import com.cardpaymentsystem.paymentservice.controller.dto.CardDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.DecryptedCardDto;
import com.cardpaymentsystem.paymentservice.exception.ValidationException;
import com.cardpaymentsystem.paymentservice.service.ApiService;
import com.cardpaymentsystem.paymentservice.service.CardService;
import com.cardpaymentsystem.paymentservice.util.EncryptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

	private final ApiService apiService;
	private final EncryptionUtil encryptionUtil;
	private static final int CARD_NUMBER_LENGTH = 16;
	private static final int EXPIRATION_DATE_LENGTH = 4;
	private static final int USER_CI_LENGTH = 88;
	private static final int BASE_YEAR = 2000;
	private static final int MIN_MONTH = 1;
	private static final int MAX_MONTH = 12;

	public CardResponseDto registerCard(CardRequestDto cardRequestDto) throws Exception {

		return apiService.registerCard(validateRegisterCard(cardRequestDto));
	}

	public DecryptedCardDto searchCard(String cardRefId) throws Exception {

		if (cardRefId == null || cardRefId.isBlank()) {
			throw new ValidationException("No card reference ID");
		}

		CardDto cardDto = apiService.searchCard(cardRefId);
		return DecryptedCardDto.builder()
			.cardRefId(cardDto.getCardRefId())
			.cardNumber(encryptionUtil.decrypt(cardDto.getEncryptedCardNumber()))
			.expirationYearMonth(cardDto.getExpirationYearMonth())
			.userCi(encryptionUtil.decrypt(cardDto.getEncryptedUserCi()))
			.createdDate(cardDto.getCreatedDate())
			.updatedDate(cardDto.getUpdatedDate())
			.build();
	}

	private CardRequestDto validateRegisterCard(CardRequestDto cardRequestDto) throws Exception {

		String cardNumber = cardRequestDto.getCardNumber();
		if (cardNumber == null || cardNumber.isBlank()) {
			throw new ValidationException("No card number");
		}
		String cleanedCardNumber = cardNumber.replaceAll("[^0-9]+", "");
		if (cleanedCardNumber.length() != CARD_NUMBER_LENGTH) {
			throw new ValidationException("Card number is not 16 digits long");
		}
		if (!applyLuhnAlgorithm(cleanedCardNumber)) {
			throw new ValidationException("Invalid card number");
		}
		String expirationYearMonth = cardRequestDto.getExpirationYearMonth();
		if (expirationYearMonth == null || expirationYearMonth.isBlank()) {
			throw new ValidationException("No expiration month/year");
		}
		String cleandExpirationYearMonth = expirationYearMonth.replaceAll("[^0-9]+", "");
		if (cleandExpirationYearMonth.length() != EXPIRATION_DATE_LENGTH) {
			throw new ValidationException("Expiration date is not 4 digits (MMYY) long");
		}
		int month = Integer.parseInt(cleandExpirationYearMonth.substring(0, 2));
		if (month < MIN_MONTH || month > MAX_MONTH) {
			throw new ValidationException("Invalid month");
		}
		int year = Integer.parseInt(cleandExpirationYearMonth.substring(2));
		if (YearMonth.of(year + BASE_YEAR, month).isBefore(YearMonth.now())) {
			throw new ValidationException("Card has expired");
		}
		String userCi = cardRequestDto.getUserCi();
		if (userCi == null || userCi.isBlank()) {
			throw new ValidationException("No user CI");
		}
		String cleanedUserCi = userCi.replaceAll("\\s+", "");
		if (cleanedUserCi.length() != USER_CI_LENGTH) {
			throw new ValidationException("User CI is not 88 bytes long");
		}

		return CardRequestDto.builder()
			.cardNumber(cleanedCardNumber)
			.expirationYearMonth(cleandExpirationYearMonth)
			.userCi(cleanedUserCi)
			.build();
	}

	private boolean applyLuhnAlgorithm(String cardNumber) {

		int sum = 0;
		boolean alternate = false;
		for (int i = cardNumber.length() - 1; i >= 0; i--) {
			int digit = Integer.parseInt(cardNumber.substring(i, i + 1));
			if (alternate) {
				digit *= 2;
				if (digit > 9) {
					digit = (digit % 10) + 1;
				}
			}
			sum += digit;
			alternate = !alternate;
		}

		return (sum % 10 == 0);
	}
}
