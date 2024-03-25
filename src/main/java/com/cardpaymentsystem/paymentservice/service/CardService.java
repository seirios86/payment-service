package com.cardpaymentsystem.paymentservice.service;

import com.cardpaymentsystem.paymentservice.controller.dto.CardRequestDto;
import com.cardpaymentsystem.paymentservice.controller.dto.CardResponseDto;
import com.cardpaymentsystem.paymentservice.controller.dto.DecryptedCardDto;

public interface CardService {

	CardResponseDto registerCard(CardRequestDto cardRequestDto) throws Exception;

	DecryptedCardDto searchCard(String cardRefId) throws Exception;
}
