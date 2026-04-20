package com.picpay.payment_system.modules.client.services;

import com.picpay.payment_system.modules.client.dto.ClientLoginRequestDTO;
import com.picpay.payment_system.modules.client.dto.ClientRequestBalanceDTO;
import com.picpay.payment_system.modules.client.dto.ClientResponseBalanceDTO;
import com.picpay.payment_system.modules.client.entities.ClientEntity;
import com.picpay.payment_system.modules.client.entities.CustomerType;
import com.picpay.payment_system.modules.client.exceptions.ClientAlreadyExistsException;
import com.picpay.payment_system.modules.client.repositories.ClientRepository;
import com.picpay.payment_system.modules.transaction.entities.TransactionalEntity;
import com.picpay.payment_system.modules.transaction.entities.TransactionalStatus;
import com.picpay.payment_system.modules.transaction.repositories.TransactionalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final TransactionalRepository transactionalRepository;
    private final ClientRepository clientRepository;

    public ClientEntity registerClient(ClientEntity clientEntity){
        this.verifyClientAlreadyExists(clientEntity.getEmail(), clientEntity.getCpf());
        this.clientRepository.save(clientEntity);
        return clientEntity;
    }
    public void verifyClientAlreadyExists(String email, String cpf){
        Optional<ClientEntity> isClientAlreadyExistsByEmail = this.clientRepository.findByEmail(email);
        Optional<ClientEntity> isClientAlreadyExistsByCpf = this.clientRepository.findByCpf(cpf);
        if(isClientAlreadyExistsByCpf.isPresent() && isClientAlreadyExistsByEmail.isPresent()){
            throw new ClientAlreadyExistsException("Email e cpf já estão em uso, acesse sua conta");
        }
        else if(isClientAlreadyExistsByEmail.isPresent()){
           throw new ClientAlreadyExistsException("Este email já está em uso");
        }
        else if(isClientAlreadyExistsByCpf.isPresent()){
            throw new ClientAlreadyExistsException("Cpf já registrado");
        }
    }
    @Transactional
    public ClientResponseBalanceDTO addBalance(ClientRequestBalanceDTO request){
        System.out.println(request);
        ClientEntity client = this.clientRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new IllegalArgumentException("CPF inválido, digite um cpf válido!."));
        if(client.getCustomerType() == CustomerType.INDIVIDUAL_CLIENT){
            BigDecimal currentBalance = client.getBalance();
            BigDecimal depositAmount = request.value();
            client.setBalance(currentBalance.add(depositAmount));
        }else {
            throw new IllegalArgumentException("Somente contas do tipo pessoa física podem realizar depósito!");
        }
        this.clientRepository.save(client);
        TransactionalEntity deposit = new TransactionalEntity();
        deposit.setPayee(client);
        deposit.setPayer(null);
        deposit.setValue(request.value());
        deposit.setStatus(TransactionalStatus.SUCESS);
        deposit.setCreatedAt(LocalDateTime.now());
        this.transactionalRepository.save(deposit);
        return new ClientResponseBalanceDTO(client.getBalance());
    }

    public ClientEntity loginUser(ClientLoginRequestDTO request) {
        Optional<ClientEntity> clientOptional = this.clientRepository.findByEmail(request.email());

        if (clientOptional.isPresent()) {
            ClientEntity client = clientOptional.get();
            if (client.getPassword().equals(request.password())) {
                return client;
            }
        }
        throw new RuntimeException("E-mail ou senha incorretos.");
    }
}

