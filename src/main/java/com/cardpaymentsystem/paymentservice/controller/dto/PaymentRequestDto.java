package com.cardpaymentsystem.paymentservice.controller.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

	private String cardRefId;
	private String userCi;
	private BigDecimal paymentAmount;
	private String storeId;
}
