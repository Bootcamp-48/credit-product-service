package com.bootcamp.creditproductservice.webclient;

import com.bootcamp.creditproductservice.webclient.model.Customer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface CustomerWebClient {
    Mono<Customer> getCustomerById(String customerId);
}
