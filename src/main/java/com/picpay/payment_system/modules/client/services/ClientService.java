package com.picpay.payment_system.modules.client.services;

import com.picpay.payment_system.modules.client.entities.ClientEntity;
import com.picpay.payment_system.modules.client.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    @Autowired
    private final ClientRepository clientRepository;

    public ClientEntity registerClient(ClientEntity clientEntity){
        this.clientRepository.save(clientEntity);
        return clientEntity;
    }
}
