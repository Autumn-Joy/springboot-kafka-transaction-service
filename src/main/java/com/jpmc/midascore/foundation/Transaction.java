package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private long senderId;
    private long recipientId;
    private float amount;
    private float incentiveAmount;


    public Transaction() {
    }

    public Transaction(long senderId, long recipientId, float amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
    }

    public Transaction(long senderId, long recipientId, float amount, float incentiveAmount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentiveAmount = incentiveAmount;
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

    public float getIncentiveAmount() {
        return incentiveAmount;
    }

    public void setIncentiveAmount(float incentiveAmount) {
        this.incentiveAmount = incentiveAmount;
    }

    @Override
    public String toString() {
        return "Transaction {senderId=" + senderId + ", recipientId=" + recipientId + ", amount=" + amount + "}";
    }
}
