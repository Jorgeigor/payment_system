package com.picpay.payment_system.modules.transaction.dto;

import java.math.BigDecimal;

public record TransactionalResponseDTO(BigDecimal value, String cpf, boolean notificationSent) {
}
