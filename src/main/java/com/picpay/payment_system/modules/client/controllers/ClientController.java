package com.picpay.payment_system.modules.client.controllers;

import com.picpay.payment_system.modules.client.dto.ClientLoginRequestDTO;
import com.picpay.payment_system.modules.client.dto.ClientRequestBalanceDTO;
import com.picpay.payment_system.modules.client.dto.ClientResponseBalanceDTO;
import com.picpay.payment_system.modules.client.entities.ClientEntity;
import com.picpay.payment_system.modules.client.repositories.ClientRepository;
import com.picpay.payment_system.modules.client.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    @PostMapping("/register")
    public ResponseEntity<ClientEntity> registeredClient(@RequestBody ClientEntity clientEntity){
        ClientEntity response = this.clientService.registerClient(clientEntity);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody ClientLoginRequestDTO request) {
        try {
            ClientEntity logado = clientService.loginUser(request);
            return ResponseEntity.ok(logado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/addBalance")
    public ResponseEntity<ClientResponseBalanceDTO> addBalance(@RequestBody ClientRequestBalanceDTO client){
        ClientResponseBalanceDTO response = this.clientService.addBalance(client);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ClientEntity> getBalance(@PathVariable String cpf){
        ClientEntity client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return ResponseEntity.ok(client);
    }
}
