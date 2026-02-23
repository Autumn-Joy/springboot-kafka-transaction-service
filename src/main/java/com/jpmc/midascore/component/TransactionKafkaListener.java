package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

// this is the service layer that receives the transaction messages from kafka
// validates the transactions
// and calls the transaction repository to save valid transactions to the database

@Component
public class TransactionKafkaListener {

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {







        System.out.println("Received message: " + transaction);
    }
}