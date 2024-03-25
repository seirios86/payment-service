package com.cardpaymentsystem.paymentservice.controller.dto;

import java.math.BigDecimal;
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
public class PaymentDto {

	private Long paymentId;
	private String cardRefId;
	private BigDecimal paymentAmount;
	private String storeId;
	private Boolean isApproved;
	private Long approvalId;
	private LocalDateTime approvedDate;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
}
