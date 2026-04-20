package com.picpay.payment_system.modules.transaction.dto;

import java.math.BigDecimal;

public record StatementItemDTO(
        String id,
        String name,
        String type,
        String date,
        String description,
        BigDecimal amount
) {}
