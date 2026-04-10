package com.picpay.payment_system.modules.client.dto;

import java.math.BigDecimal;

public record ClientRequestBalanceDTO(String cpf, BigDecimal value) {
}
