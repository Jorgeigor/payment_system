package com.picpay.payment_system.modules.transaction.repositories;

import com.picpay.payment_system.modules.transaction.entities.TransactionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface TransactionalRepository extends JpaRepository<TransactionalEntity, UUID> {
}
