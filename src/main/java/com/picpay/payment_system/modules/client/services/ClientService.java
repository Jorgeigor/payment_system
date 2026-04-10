package com.picpay.payment_system.modules.client.services;

import com.picpay.payment_system.modules.client.dto.ClientRequestBalanceDTO;
import com.picpay.payment_system.modules.client.dto.ClientResponseBalanceDTO;
import com.picpay.payment_system.modules.client.entities.ClientEntity;
import com.picpay.payment_system.modules.client.entities.CustomerType;
import com.picpay.payment_system.modules.client.exceptions.ClientAlreadyExistsException;
import com.picpay.payment_system.modules.client.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    @Autowired
    private final ClientRepository clientRepository;

    public ClientEntity registerClient(ClientEntity clientEntity){
        this.verifyClientAlreadyExists(clientEntity.getEmail(), clientEntity.getCpf());
        this.clientRepository.save(clientEntity);
        return clientEntity;
    }

    public void verifyClientAlreadyExists(String email, String cpf){
        Optional<ClientEntity> isClientAlredyExistsByEmail = this.clientRepository.findByEmail(email);
        Optional<ClientEntity> isClientAlredyExistsByCpf = this.clientRepository.findByCpf(cpf);
        if(isClientAlredyExistsByCpf.isPresent() && isClientAlredyExistsByEmail.isPresent()){
            throw new ClientAlreadyExistsException("Email e cpf já estão em uso, acesse sua conta");
        }
        else if(isClientAlredyExistsByEmail.isPresent()){
           throw new ClientAlreadyExistsException("Este email já está em uso");
        }
        else if(isClientAlredyExistsByCpf.isPresent()){
            throw new ClientAlreadyExistsException("Cpf já registrado");
        }
    }

    public ClientResponseBalanceDTO addBalance(ClientRequestBalanceDTO request){
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

        return new ClientResponseBalanceDTO(client.getBalance());
    }
}
