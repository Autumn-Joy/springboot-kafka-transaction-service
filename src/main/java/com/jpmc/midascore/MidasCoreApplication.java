package com.jpmc.midascore;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MidasCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidasCoreApplication.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(MidasCoreApplication.class);


    @Bean
    public CommandLineRunner demo(TransactionRecordRepository repository) {
        return (args) -> {
            // save a few transaction records
            repository.save(new TransactionRecord(new Transaction(1L, 2L, 100.0f)));
            repository.save(new TransactionRecord(new Transaction(2L, 3L, 250.5f)));
            repository.save(new TransactionRecord(new Transaction(1L, 3L, 75.0f)));
            repository.save(new TransactionRecord(new Transaction(3L, 1L, 500.0f)));
            repository.save(new TransactionRecord(new Transaction(2L, 1L, 150.25f)));

            // fetch all transaction records
            logger.info("Transaction records found with findAll():");
            logger.info("-------------------------------");
            repository.findAll().forEach(record -> {
                logger.info(record.toString());
            });
            logger.info("");
        };
    }

}
