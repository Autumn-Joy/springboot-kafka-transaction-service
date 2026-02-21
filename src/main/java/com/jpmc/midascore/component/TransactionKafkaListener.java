package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TransactionKafkaListener {

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transactionInfo) {
        System.out.println("Received message: " + transactionInfo);
    }
}