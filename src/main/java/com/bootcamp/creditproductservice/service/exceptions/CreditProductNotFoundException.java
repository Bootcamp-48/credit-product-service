package com.bootcamp.creditproductservice.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class CreditProductNotFoundException extends RuntimeException {

    public CreditProductNotFoundException(String id) {
        super("Credit product not found with ID: " + id);
    }
}