package com.jpmc.midascore.service;

import com.jpmc.midascore.component.TransactionIncentiveClient;
import com.jpmc.midascore.component.ValidateTransactionService;
import com.jpmc.midascore.dto.TransactionIncentiveDTO;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final ValidateTransactionService transactionValidationService;
    private final TransactionRecordRepository transactionRecordRepository;
    private final UserRepository userRepository;
    private final TransactionIncentiveClient transactionIncentiveClient;

    public TransactionService(ValidateTransactionService transactionValidationService,
                              TransactionRecordRepository transactionRecordRepository,
                              UserRepository userRepository,
                              TransactionIncentiveClient transactionIncentiveClient)
    {
        this.transactionValidationService = transactionValidationService;
        this.transactionRecordRepository = transactionRecordRepository;
        this.userRepository = userRepository;
        this.transactionIncentiveClient = transactionIncentiveClient;
    }

    public String processTransaction(Transaction transaction) {

        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        TransactionIncentiveDTO incentive;

        //  check if the transaction is valid
        if (!transactionValidationService.isValid(transaction)) {
            System.out.println("Transaction is invalid");
            return "Invalid transaction. " +
                    "Transaction discarded. " +
                    "Please try again and ensure that " +
                    "(1) the Sender Id and Recipient Id are valid and " +
                    "(2) that the sender's balance is " +
                    "equal or above the transaction amount.";
        }

        // transaction is valid.
        System.out.println("...........");
        System.out.println("Before Post: ................");
        System.out.println("SenderRecord:" + sender);
        System.out.println("RecipientRecord:" + recipient);
        System.out.println(transaction);

        // call `post to incentive API here, returns incentive amount
        incentive = transactionIncentiveClient.postTransaction(transaction);

        // set the transaction.incentiveAmount to equal returned incentive amount
        transaction.setIncentiveAmount(incentive.getAmount());
        System.out.println("Transaction Incentive amount: " + transaction.getIncentiveAmount());

        //save transaction to the database
        transactionRecordRepository.save(new TransactionRecord(transaction));
        System.out.println("TransactionRecord:" + transaction);

        //update sender balance and SAVE TO DB!
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        userRepository.save(sender);
        System.out.println("SenderRecord:" + sender);

        //update recipient balance and SAVE TO DB!
        // add incentiveAmount as well
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + transaction.getIncentiveAmount());
        userRepository.save(recipient);
        System.out.println("RecipientRecord:" + recipient);

        return "Transaction saved to database, updated sender balance, and updated recipient balance, including incentive amount.";
    }

    public void returnTestValue() {
        System.out.println("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
        System.out.println("Returning test value: ");
        System.out.println(userRepository.findById(9));
    }

}
