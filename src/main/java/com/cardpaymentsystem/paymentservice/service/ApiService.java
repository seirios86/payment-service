package com.cardpaymentsystem.paymentservice.service;

import com.cardpaymentsystem.paymentservice.controller.dto.ApprovalRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.ApprovalResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.TokenRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.TokenResponseDto;

public interface ApiService {

	CardResponseDto registerCard(CardRequestDto cardRequestDto) throws Exception;

	CardDto searchCard(String cardRefId) throws Exception;

	TokenResponseDto createToken(TokenRequestDto tokenRequestDto) throws Exception;

	ApprovalResponseDto requestApproval(ApprovalRequestDto approvalRequestDto) throws Exception;

	void deleteToken(String token) throws Exception;
}
