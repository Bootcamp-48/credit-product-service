package com.bootcamp.creditproductservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreditCard extends CreditProduct {
    private String cardNumber;
    private double creditLimit;
    private double currentBalance;
    private LocalDate expirationDate;

}