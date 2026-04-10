package com.picpay.payment_system.modules.transaction.services;

import com.picpay.payment_system.modules.api.ApiResponseDTO;
import com.picpay.payment_system.modules.client.entities.ClientEntity;
import com.picpay.payment_system.modules.client.repositories.ClientRepository;
import com.picpay.payment_system.modules.transaction.dto.TransactionalRequestDTO;
import com.picpay.payment_system.modules.transaction.dto.TransactionalResponseDTO;
import com.picpay.payment_system.modules.transaction.entities.TransactionalStatus;
import com.picpay.payment_system.modules.transaction.entities.TransactionalEntity;
import com.picpay.payment_system.modules.transaction.repositories.TransactionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionalService {
    private final TransactionalRepository transactionalRepository;
    private final ClientRepository clientRepository;

    public TransactionalResponseDTO makeTransfer(TransactionalRequestDTO request) {
        System.out.println("O pagador que chegou foi: " + request.payer());
        ClientEntity payerEntity = this.clientRepository.findByCpf(request.payer())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado no sistema."));

        ClientEntity payeeEntity = this.clientRepository.findByCpf(request.payee())
                .orElseThrow(() -> new IllegalArgumentException("Vendedor não encontrado."));
        this.queryApi();
        if(payerEntity.getBalance().compareTo(request.value()) < 0){
            throw new IllegalArgumentException("Adicione saldo a sua conta para continuar essa operação!");
        }
        TransactionalEntity transfer = new TransactionalEntity();
        transfer.setPayee(payeeEntity);
        transfer.setPayer(payerEntity);
        transfer.setValue(request.value());
        transfer.setStatus(TransactionalStatus.SUCESS);
        transfer.setCreatedAt(LocalDateTime.now());
        this.transactionalRepository.save(transfer);
        return new TransactionalResponseDTO(transfer.getValue(), payeeEntity.getCpf());
    }

    public void queryApi(){
        RestTemplate restTemplate = new RestTemplate();
        ApiResponseDTO authResponse = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", ApiResponseDTO.class);
        if( authResponse == null || !"success".equalsIgnoreCase(authResponse.status())){
            throw new IllegalArgumentException("Sistema com instabilidade tente em alguns instantes!");
        }

    }

}
