package com.picpay.payment_system.modules.client.controllers;

import com.picpay.payment_system.modules.client.entities.ClientEntity;
import com.picpay.payment_system.modules.client.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientEntity> setClient(@RequestBody ClientEntity clientEntity){
        ClientEntity response = this.clientService.registerClient(clientEntity);
        return ResponseEntity.ok().body(response);
    }
}
