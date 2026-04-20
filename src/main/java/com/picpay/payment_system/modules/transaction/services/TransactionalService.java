package com.picpay.payment_system.modules.transaction.services;

import com.picpay.payment_system.modules.api.ApiResponseDTO;
import com.picpay.payment_system.modules.client.dto.ClientTransactionalListResponseDTO;
import com.picpay.payment_system.modules.client.entities.ClientEntity;
import com.picpay.payment_system.modules.client.repositories.ClientRepository;
import com.picpay.payment_system.modules.transaction.dto.NotificationRequestDTO;
import com.picpay.payment_system.modules.transaction.dto.StatementItemDTO;
import com.picpay.payment_system.modules.transaction.dto.TransactionalRequestDTO;
import com.picpay.payment_system.modules.transaction.dto.TransactionalResponseDTO;
import com.picpay.payment_system.modules.transaction.entities.TransactionalStatus;
import com.picpay.payment_system.modules.transaction.entities.TransactionalEntity;
import com.picpay.payment_system.modules.transaction.repositories.TransactionalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TransactionalService {
    private final TransactionalRepository transactionalRepository;
    private final ClientRepository clientRepository;
    @Transactional
    public TransactionalResponseDTO makeTransfer(TransactionalRequestDTO request) {
        System.out.println("Dados recebidos:  " + request.payer());

        ClientEntity payerEntity = this.clientRepository.findByCpf(request.payer())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado no sistema."));

        ClientEntity payeeEntity = this.clientRepository.findByCpf(request.payee())
                .orElseThrow(() -> new IllegalArgumentException("Vendedor não encontrado."));
        this.queryApi();
        if(payerEntity.getBalance().compareTo(request.value()) < 0){
            throw new IllegalArgumentException("Adicione saldo a sua conta para continuar essa operação!");
        }

        payerEntity.setBalance(payerEntity.getBalance().subtract(request.value()));
        payeeEntity.setBalance(payeeEntity.getBalance().add(request.value()));

        this.clientRepository.save(payerEntity);
        this.clientRepository.save(payeeEntity);

        TransactionalEntity transfer = new TransactionalEntity();
        transfer.setPayee(payeeEntity);
        transfer.setPayer(payerEntity);
        transfer.setValue(request.value());
        transfer.setStatus(TransactionalStatus.SUCESS);
        transfer.setCreatedAt(LocalDateTime.now());
        this.transactionalRepository.save(transfer);

        boolean isNotified = this.sendNotification(payeeEntity, request.value());

        return new TransactionalResponseDTO(transfer.getValue(), payeeEntity.getCpf(), isNotified);
    }

    public ClientTransactionalListResponseDTO getStatementByCpf(String cpf) {

        clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        List<TransactionalEntity> transactions = this.transactionalRepository
                .findByPayerCpfOrPayeeCpfOrderByCreatedAtDesc(cpf, cpf);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy '—' HH:mm", Locale.US);

        List<StatementItemDTO> items = transactions.stream().map(tx -> {

            String type;
            String otherPartyName;
            String description;

            if (tx.getPayer() == null) {
                type = "received";
                otherPartyName = "Adição de Saldo";
                description = "Depósito via Pix/Boleto";
            }
            else if (tx.getPayer().getCpf().equals(cpf)) {
                type = "sent";
                otherPartyName = tx.getPayee().getName();
                description = "Transferência enviada";
            }
            else {
                type = "received";
                otherPartyName = tx.getPayer().getName();
                description = "Transferência recebida";
            }

            return new StatementItemDTO(
                    tx.getTransactionalId().toString(),
                    otherPartyName,
                    type,
                    tx.getCreatedAt().format(formatter),
                    description,
                    tx.getValue()
            );

        }).toList();
        return new ClientTransactionalListResponseDTO(items);
    }

    public void queryApi(){
        RestTemplate restTemplate = new RestTemplate();
        ApiResponseDTO authResponse = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", ApiResponseDTO.class);
        if( authResponse == null || !"success".equalsIgnoreCase(authResponse.status())){
            throw new IllegalArgumentException("Sistema com instabilidade tente em alguns instantes!");
        }
    }

    private boolean sendNotification(ClientEntity payee, BigDecimal value){
        try {
            RestTemplate restTemplate = new RestTemplate();
            NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO(payee.getEmail(), "Você recebeu uma transferência no valor de R$" + value);
            restTemplate.postForObject("https://util.devi.tools/api/v1/notify", notificationRequestDTO, String.class);
            return true;
        } catch (Exception e) {
            System.out.println("Falha ao notificar: " + payee.getEmail());
            return false;
        }
    }

}
