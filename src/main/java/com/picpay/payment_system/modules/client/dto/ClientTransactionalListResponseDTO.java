package com.picpay.payment_system.modules.client.dto;

import com.picpay.payment_system.modules.transaction.dto.StatementItemDTO;

import java.util.List;

public record ClientTransactionalListResponseDTO(List<StatementItemDTO> transactions) {
}
