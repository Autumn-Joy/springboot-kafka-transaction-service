package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// this is the service layer that receives the transaction messages from kafka
// validates the transactions
// and calls the transaction repository to save valid transactions to the database

@Component
public class TransactionKafkaListener {

    private final TransactionService transactionService;

    public TransactionKafkaListener(TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void receiveTransactions(Transaction transaction) {

        transactionService.processTransaction(transaction);

        // TO DO: test 3 passes for this; remove later
        transactionService.returnTestValue();

    }
}