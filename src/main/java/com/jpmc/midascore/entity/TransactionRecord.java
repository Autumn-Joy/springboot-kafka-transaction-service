package com.jpmc.midascore.entity;

import com.jpmc.midascore.foundation.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

// transactions have a many-to-one relationship to sender and recipient User entities

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue()
    private Long id;

    @Column(nullable = false)
    private long senderId;

    @Column(nullable = false)
    private long recipientId;

    @Column(nullable = false)
    private float amount;

    protected TransactionRecord() {
    }

    public TransactionRecord(Transaction transaction) {
        this.senderId = transaction.getSenderId();
        this.recipientId = transaction.getRecipientId();
        this.amount = transaction.getAmount();
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}
