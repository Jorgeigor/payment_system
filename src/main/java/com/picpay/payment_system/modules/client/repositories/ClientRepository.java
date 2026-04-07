package com.picpay.payment_system.modules.client.repositories;

import com.picpay.payment_system.modules.client.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {
    Optional<ClientEntity> findByEmailOrCpf(String email, String cpf);
}
