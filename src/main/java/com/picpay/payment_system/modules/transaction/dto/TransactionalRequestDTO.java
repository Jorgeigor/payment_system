package com.picpay.payment_system.modules.transaction.dto;


import com.picpay.payment_system.modules.api.ApiResponseDTO;

import java.math.BigDecimal;

public record TransactionalRequestDTO(BigDecimal value, String payer, String payee) {
}
