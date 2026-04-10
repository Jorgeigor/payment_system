package com.picpay.payment_system.modules.transaction.dto;

import com.picpay.payment_system.modules.transaction.entities.TransactionalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TrabsactionalDetails(BigDecimal value, TransactionalStatus status, String id, LocalDateTime createdAt, String payeeCpf, String payerCpf) {
}
