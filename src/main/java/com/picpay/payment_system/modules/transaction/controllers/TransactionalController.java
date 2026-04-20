package com.picpay.payment_system.modules.transaction.controllers;

import com.picpay.payment_system.modules.client.dto.ClientTransactionalListResponseDTO;
import com.picpay.payment_system.modules.transaction.dto.TransactionalRequestDTO;
import com.picpay.payment_system.modules.transaction.dto.TransactionalResponseDTO;
import com.picpay.payment_system.modules.transaction.services.TransactionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactional")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TransactionalController {
    private final TransactionalService transactionalService;

    @PostMapping("/register")
    public ResponseEntity<TransactionalResponseDTO> registeredTransactional(@RequestBody TransactionalRequestDTO request){
        TransactionalResponseDTO response = this.transactionalService.makeTransfer(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/statement/{cpf}")
    public ResponseEntity<ClientTransactionalListResponseDTO> getStatement(@PathVariable String cpf) {

        ClientTransactionalListResponseDTO response = this.transactionalService.getStatementByCpf(cpf);

        return ResponseEntity.ok().body(response);
    }
}
