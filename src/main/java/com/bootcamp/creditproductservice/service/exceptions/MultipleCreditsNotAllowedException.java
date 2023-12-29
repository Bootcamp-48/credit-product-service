package com.bootcamp.creditproductservice.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MultipleCreditsNotAllowedException extends RuntimeException {

    public MultipleCreditsNotAllowedException(String message) {
        super(message);
    }
}