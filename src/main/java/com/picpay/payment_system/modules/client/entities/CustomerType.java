package com.picpay.payment_system.modules.client.entities;

public enum CustomerType {
    INDIVIDUAL_CLIENT("PF", "Pessoa fisica"),
    CORPORATE_CLIENT("PJ", "Pessoa juridica");

    private final String customer_type;
    private final String customer_code;

    CustomerType(String customerCode, String customerType){
        customer_type = customerType;
        customer_code = customerCode;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public String getCustomer_code() {
        return customer_code;
    }
}
