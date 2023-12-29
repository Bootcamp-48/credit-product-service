package com.bootcamp.creditproductservice.webclient.impl;

import com.bootcamp.creditproductservice.webclient.CustomerWebClient;
import com.bootcamp.creditproductservice.webclient.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class CustomerWebClientImpl implements CustomerWebClient {

    private final WebClient webClient;

    public CustomerWebClientImpl(@Value("${customer.service.url}") String accountServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(accountServiceUrl)
                .build();
    }

    @Override
    public Mono<Customer> getCustomerById(String customerId) {
        return webClient.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(Customer.class);
    }
}