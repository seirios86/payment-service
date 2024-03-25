package com.cardpaymentsystem.paymentservice.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cardpaymentsystem.paymentservice.controller.dto.CardDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.PaymentSearchDto;
import com.cardpaymentsystem.paymentservice.service.CardService;
import com.cardpaymentsystem.paymentservice.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Payment Service")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

	private final PaymentService paymentService;
	private final CardService cardService;

	@Operation(summary = "Register Card")
	@Parameter(name = "cardNumber", required = true, description = "card number")
	@Parameter(name = "expirationYearMonth", required = true, description = "card expiration year/month (MMYY)")
	@Parameter(name = "userCi", required = true, description = "user CI")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
	})
	@PostMapping("/card")
	public ResponseEntity<?> registarCard(@RequestBody CardRequestDto cardRequestDto) throws Exception {

		return ResponseEntity.status(HttpStatus.OK).body(cardService.registerCard(cardRequestDto));
	}

	@Operation(summary = "Search Card")
	@Parameter(name = "cardRefId", required = true, description = "card reference ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardDto.class))),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
	})
	@GetMapping("/card")
	public ResponseEntity<?> searchCard(@RequestParam String cardRefId) throws Exception {

		return ResponseEntity.status(HttpStatus.OK).body(cardService.searchCard(cardRefId));
	}

	@Operation(summary = "Request Payment")
	@Parameter(name = "cardRefId", required = true, description = "card reference ID")
	@Parameter(name = "userCi", required = true, description = "user CI")
	@Parameter(name = "paymentAmount", required = true, description = "payment amount")
	@Parameter(name = "storeId", required = true, description = "store ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
	})
	@PostMapping("")
	public ResponseEntity<?> requestPayment(@RequestBody PaymentRequestDto paymentRequestDto) throws Exception {

		return ResponseEntity.status(HttpStatus.OK).body(paymentService.requestPayment(paymentRequestDto));
	}

	@Operation(summary = "Search Payment")
	@Parameter(name = "startDate", required = true, description = "start date (YYYYMMDD)")
	@Parameter(name = "endDate", required = true, description = "end date (YYYYMMDD)")
	@Parameter(name = "cardRefId", description = "card reference ID")
	@Parameter(name = "minAmount", description = "minimum payment amount")
	@Parameter(name = "maxAmount", description = "maximum payment amount")
	@Parameter(name = "storeId", description = "store ID")
	@Parameter(name = "isApproved", description = "approval status")
	@Parameter(name = "approvalId", description = "approval ID")
	@Parameter(name = "approvedStartDate", description = "approval start date (YYYYMMDD)")
	@Parameter(name = "approvedEndDate", description = "approcal end date (YYYYMMDD)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDto.class))),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
		@ApiResponse(responseCode = "404", description = "Not Found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
	})
	@GetMapping(value = "")
	public ResponseEntity<?> searchPayment(PaymentSearchDto paymentSearchDto) throws Exception {

		return ResponseEntity.status(HttpStatus.OK).body(paymentService.searchPayment(paymentSearchDto));
	}
}
