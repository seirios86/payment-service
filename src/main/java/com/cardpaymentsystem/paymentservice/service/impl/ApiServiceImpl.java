package com.cardpaymentsystem.paymentservice.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.cardpaymentsystem.paymentservice.controller.dto.ApprovalRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.ApprovalResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.TokenRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.TokenResponseDto;
import com.cardpaymentsystem.paymentservice.exception.ApiCallException;
import com.cardpaymentsystem.paymentservice.service.ApiService;
import com.cardpaymentsystem.paymentservice.service.DiscoveryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {

	private final WebClient.Builder webClientBuilder;
	private final DiscoveryService discoveryService;
	private final ObjectMapper objectMapper;
	private static final String CARD_REGISTRAION_ERROR_PREFIX = "Card registration failed: ";
	private static final String CARD_SEARCH_ERROR_PREFIX = "Card search failed: ";
	private static final String TOKEN_ISSUANCE_ERROR_PREFIX = "Token issuance failed: ";
	private static final String PAYMENT_APPROVAL_ERROR_PREFIX = "Payment approval failed: ";
	private static final String TOKEN_DELETION_ERROR_PREFIX = "Token deletion failed: ";
	private static final String FAILED_TO_RECEIVE_RESPONSE = "Failed to receive a response.";

	public CardResponseDto registerCard(CardRequestDto cardRequestDto) throws Exception {

		ResponseEntity<String> response;
		try {
			response = webClientBuilder.baseUrl(discoveryService.getApiGatewayUrl())
				.build()
				.post()
				.uri("/api/v1/token/card")
				.bodyValue(cardRequestDto)
				.retrieve()
				.toEntity(String.class)
				.block();

		} catch (WebClientResponseException e) {
			ProblemDetail problemDetail = objectMapper.readValue(e.getResponseBodyAsString(), ProblemDetail.class);
			throw new ApiCallException(CARD_REGISTRAION_ERROR_PREFIX + problemDetail.getDetail(),
				problemDetail.getStatus());
		}

		if (response == null) {
			throw new ApiCallException(CARD_REGISTRAION_ERROR_PREFIX + FAILED_TO_RECEIVE_RESPONSE,
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return objectMapper.readValue(response.getBody(), CardResponseDto.class);
	}

	public CardDto searchCard(String cardRefId) throws Exception {

		ResponseEntity<String> response;
		try {
			response = webClientBuilder.baseUrl(discoveryService.getApiGatewayUrl())
				.build()
				.get()
				.uri(uriBuilder -> uriBuilder
					.path("/api/v1/token/card")
					.queryParam("cardRefId", cardRefId)
					.build())
				.retrieve()
				.toEntity(String.class)
				.block();

		} catch (WebClientResponseException e) {
			ProblemDetail problemDetail = objectMapper.readValue(e.getResponseBodyAsString(), ProblemDetail.class);
			throw new ApiCallException(CARD_SEARCH_ERROR_PREFIX + problemDetail.getDetail(),
				problemDetail.getStatus());
		}

		if (response == null) {
			throw new ApiCallException(CARD_SEARCH_ERROR_PREFIX + FAILED_TO_RECEIVE_RESPONSE,
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return objectMapper.readValue(response.getBody(), CardDto.class);
	}

	public TokenResponseDto createToken(TokenRequestDto tokenRequestDto) throws Exception {

		ResponseEntity<String> response;
		try {
			response = webClientBuilder.baseUrl(discoveryService.getApiGatewayUrl())
				.build()
				.post()
				.uri("/api/v1/token")
				.bodyValue(tokenRequestDto)
				.retrieve()
				.toEntity(String.class)
				.block();

		} catch (WebClientResponseException e) {
			ProblemDetail problemDetail = objectMapper.readValue(e.getResponseBodyAsString(), ProblemDetail.class);
			throw new ApiCallException(TOKEN_ISSUANCE_ERROR_PREFIX + problemDetail.getDetail(),
				problemDetail.getStatus());
		}

		if (response == null) {
			throw new ApiCallException(TOKEN_ISSUANCE_ERROR_PREFIX + FAILED_TO_RECEIVE_RESPONSE,
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return objectMapper.readValue(response.getBody(), TokenResponseDto.class);
	}

	public ApprovalResponseDto requestApproval(ApprovalRequestDto approvalRequestDto) throws Exception {

		ResponseEntity<String> response;
		try {
			response = webClientBuilder.baseUrl(discoveryService.getApiGatewayUrl())
				.build()
				.post()
				.uri("/api/v1/approval")
				.bodyValue(approvalRequestDto)
				.retrieve()
				.toEntity(String.class)
				.block();

		} catch (WebClientResponseException e) {
			ProblemDetail problemDetail = objectMapper.readValue(e.getResponseBodyAsString(), ProblemDetail.class);
			throw new ApiCallException(PAYMENT_APPROVAL_ERROR_PREFIX + problemDetail.getDetail(),
				problemDetail.getStatus());
		}

		if (response == null) {
			throw new ApiCallException(PAYMENT_APPROVAL_ERROR_PREFIX + FAILED_TO_RECEIVE_RESPONSE,
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return objectMapper.readValue(response.getBody(), ApprovalResponseDto.class);
	}

	public void deleteToken(String token) throws Exception {

		ResponseEntity<String> response;
		try {
			response = webClientBuilder.baseUrl(discoveryService.getApiGatewayUrl())
				.build()
				.delete()
				.uri("/api/v1/token/{token}", token)
				.retrieve()
				.toEntity(String.class)
				.block();

		} catch (WebClientResponseException e) {
			ProblemDetail problemDetail = objectMapper.readValue(e.getResponseBodyAsString(), ProblemDetail.class);
			throw new ApiCallException(TOKEN_DELETION_ERROR_PREFIX + problemDetail.getDetail(),
				problemDetail.getStatus());
		}

		if (response == null) {
			throw new ApiCallException(TOKEN_DELETION_ERROR_PREFIX + FAILED_TO_RECEIVE_RESPONSE,
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
}
