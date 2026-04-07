package com.picpay.payment_system.modules.client.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@Entity(name = "Client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String cpf;

    @Email(message = "O campo [email] deve conter um e-mail válido")
    private String email;

    @Length(min = 8, max = 12)
    private String password;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    private CustomerType customerCode;
}
