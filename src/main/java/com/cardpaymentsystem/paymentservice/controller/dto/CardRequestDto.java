package com.cardpaymentsystem.paymentservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {

	private String cardNumber;
	private String expirationYearMonth;
	private String userCi;
}
