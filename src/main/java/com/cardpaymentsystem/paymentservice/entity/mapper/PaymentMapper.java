package com.cardpaymentsystem.paymentservice.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cardpaymentsystem.paymentservice.controller.dto.PaymentDto;
import com.cardpaymentsystem.paymentservice.entity.Payment;

@Mapper
public interface PaymentMapper {

	PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

	@Mapping(source = "paymentId", target = "paymentId")
	@Mapping(source = "cardRefId", target = "cardRefId")
	@Mapping(source = "paymentAmount", target = "paymentAmount")
	@Mapping(source = "storeId", target = "storeId")
	@Mapping(source = "isApproved", target = "isApproved")
	@Mapping(source = "approvalId", target = "approvalId")
	@Mapping(source = "approvedDate", target = "approvedDate")
	@Mapping(source = "createdDate", target = "createdDate")
	@Mapping(source = "updatedDate", target = "updatedDate")
	PaymentDto toDto(Payment payment);
}
