package com.cardpaymentsystem.paymentservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	private String cardRefId;
	private BigDecimal paymentAmount;
	private String storeId;
	private Boolean isApproved;
	private Long approvalId;
	private LocalDateTime approvedDate;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	@PrePersist
	public void prePersist() {

		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {

		this.updatedDate = LocalDateTime.now();
	}

	public void approvePayment(Long approvalId) {

		this.isApproved = true;
		this.approvalId = approvalId;
		this.approvedDate = LocalDateTime.now();
	}
}
