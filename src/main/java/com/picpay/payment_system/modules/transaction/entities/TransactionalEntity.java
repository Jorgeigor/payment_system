package com.picpay.payment_system.modules.transaction.entities;

import com.picpay.payment_system.modules.client.entities.ClientEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransactionalEntity {

    private BigDecimal value;

    private TransactionalStatus status;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionalId;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private ClientEntity payer;


    @ManyToOne
    @JoinColumn(name = "payee_id")
    private ClientEntity payee;



}
