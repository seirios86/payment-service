package com.cardpaymentsystem.paymentservice.service;

import java.util.List;

import com.cardpaymentsystem.paymentservice.controller.dto.PaymentDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentSearchDto;

public interface PaymentService {
	PaymentResponseDto requestPayment(PaymentRequestDto paymentRequestDto) throws Exception;

	List<PaymentDto> searchPayment(PaymentSearchDto paymentSearchDto) throws Exception;
}
