package com.picpay.payment_system.modules.client.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;


@Getter
public enum CustomerType {
    INDIVIDUAL_CLIENT("PF", "Pessoa fisica"),
    CORPORATE_CLIENT("PJ", "Pessoa juridica");

    private final String customer_type;
    private final String customer_code;

    CustomerType(String code, String description){
        this.customer_type = description;
        this.customer_code = code;
    }

    @JsonCreator
    public static CustomerType fromString(String text){
        for(CustomerType type: CustomerType.values()){
            if(type.customer_type.equalsIgnoreCase(text) || type.customer_code.equalsIgnoreCase(text)){
            return type;
            }
        }
        throw new IllegalArgumentException(String.format("Tipo de cliente inválido: %s digite um válor válido",text));
    }

}
