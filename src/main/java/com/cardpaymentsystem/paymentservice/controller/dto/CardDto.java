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
public class CardDto {

	private String cardRefId;
	private String encryptedCardNumber;
	private String expirationYearMonth;
	private String encryptedUserCi;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
}
