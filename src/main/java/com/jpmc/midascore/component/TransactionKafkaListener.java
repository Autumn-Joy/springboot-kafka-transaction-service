package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// this is the service layer that receives the transaction messages from kafka
// validates the transactions
// and calls the transaction repository to save valid transactions to the database

@Component
public class TransactionKafkaListener {

    private final ValidateTransactionService transactionValidationService;
    private final TransactionRecordRepository transactionRecordRepository;
    private final UserRepository userRepository;

    public TransactionKafkaListener(ValidateTransactionService transactionValidationService, TransactionRecordRepository transactionRecordRepository, UserRepository userRepository) {
        this.transactionValidationService = transactionValidationService;
        this.transactionRecordRepository = transactionRecordRepository;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void receiveTransactions(Transaction transaction) {

        processTransaction(transaction);
        // TO DO: test 3 passes for this; refactor later
        findWaldorfBalance();

    }

    public String processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        // TO DO: clean up debugging statements
//        System.out.println("Sender info:" + sender);
//        System.out.println("Recipient info: " + recipient);
//        System.out.println("Transaction info: " + transaction);

        //  validate transaction
        if (!transactionValidationService.isValid(transaction)) {

            // TO DO: clean up debugging statements
//            System.out.println("Transaction is invalid");
            return "Invalid transaction. Transaction discarded.";
//            throw new Error("Invalid transaction. Transaction discarded. Please try again and ensure that (1) the Sender Id and Recipient Id are valid and (2) that the sender's balance is equal or above the transaction amount.");
        }
        System.out.println("Transaction is valid");
        //save transaction to database
        transactionRecordRepository.save(new TransactionRecord(transaction));
        System.out.println("Transaction saved to database");

        //update sender balance and SAVE TO DB!
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        userRepository.save(sender);

        //update recipient balance and SAVE TO DB!
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());
        userRepository.save(recipient);

        return "Transaction saved to database and updated sender and recipient balances.";
    }

    public void findWaldorfBalance() {
        System.out.println(userRepository.findById(5));
    }


}