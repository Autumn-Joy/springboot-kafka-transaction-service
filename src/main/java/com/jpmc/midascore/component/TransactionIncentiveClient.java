package com.jpmc.midascore.component;

import com.jpmc.midascore.dto.TransactionIncentiveDTO;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionIncentiveClient {

    private final RestTemplate restTemplate;

    // TO DO: refactor later to use application.yml or config
    private final String baseUrl = "http://localhost:8080";


    public TransactionIncentiveClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TransactionIncentiveDTO postTransaction(Transaction transaction) {
        // calls 3rd party API
        // returns response in the `TransactionIncentive` type (DTO)
        TransactionIncentiveDTO incentive;

        String url = baseUrl + "/incentive";
        incentive = restTemplate.postForObject(url, transaction, TransactionIncentiveDTO.class);

        System.out.println("Incentive amount after `POST`: " + incentive.getAmount());

        return incentive;

    }
}
