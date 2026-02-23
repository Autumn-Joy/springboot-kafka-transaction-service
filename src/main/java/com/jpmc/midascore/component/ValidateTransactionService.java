package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidateTransactionService {

    private final UserRepository userRepository;

    public ValidateTransactionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValid(Transaction transaction) {

        long senderId = transaction.getSenderId();
        long recipientId = transaction.getRecipientId();
        float transactionAmount = transaction.getAmount();
        float senderBalance = userRepository.findById(senderId).getBalance();


        if (userRepository.findById(recipientId) == null) {
            return false;
        }
        else if (userRepository.findById(senderId) == null) {
            return false;
        }
        else if (senderBalance < transactionAmount) {
            return false;
        }
        else { return true; }

    }
}
