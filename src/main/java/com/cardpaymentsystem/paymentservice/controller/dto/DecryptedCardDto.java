package com.cardpaymentsystem.paymentservice.controller.dto;

import java.time.LocalDateTime;

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
public class DecryptedCardDto {

	private String cardRefId;
	private String cardNumber;
	private String expirationYearMonth;
	private String userCi;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
}
