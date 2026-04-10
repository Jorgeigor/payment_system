package com.picpay.payment_system.modules.transaction.entities;

import lombok.Getter;

@Getter
public enum TransactionalStatus {
    SUCESS("APROVADA");
    //UNAUTHORIZED("BARRADO PELA API EXTERNA"),
    //INSUFICIENT_BALANCE("SALDO INSUFICIENTE");

    private final String description;


    TransactionalStatus(String description) {
        this.description = description;
    }


}
