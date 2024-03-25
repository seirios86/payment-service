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
public class PaymentSearchDto {

	private String startDate;
	private String endDate;
	private String cardRefId;
	private BigDecimal startAmount;
	private BigDecimal endAmount;
	private String storeId;
	private Boolean isApproved;
	private Long approvalId;
	private String approvedStartDate;
	private String approvedEndDate;
}
