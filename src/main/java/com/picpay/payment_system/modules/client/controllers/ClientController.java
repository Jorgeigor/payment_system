package com.picpay.payment_system.modules.client.controllers;

import com.picpay.payment_system.modules.client.dto.ClientRequestBalanceDTO;
import com.picpay.payment_system.modules.client.dto.ClientResponseBalanceDTO;
import com.picpay.payment_system.modules.client.entities.ClientEntity;
import com.picpay.payment_system.modules.client.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientEntity> registeredClient(@RequestBody ClientEntity clientEntity){
        ClientEntity response = this.clientService.registerClient(clientEntity);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/addBalance")
    public ResponseEntity<ClientResponseBalanceDTO> addBlance(@RequestBody ClientRequestBalanceDTO client){
        ClientResponseBalanceDTO response = this.clientService.addBalance(client);
        return ResponseEntity.ok().body(response);
    }
}
