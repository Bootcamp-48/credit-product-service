package com.bootcamp.creditproductservice.entity;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Document(collection = "credits")
public class CreditProduct {

    @Id
    private String id;

    @NotBlank(message = "Customer ID cannot be blank")
    private String customerId;

    @Positive(message = "Amount must be a positive value")
    private double amount;

    @PastOrPresent(message = "Issue date must be in the past or present")
    private LocalDate issueDate;

    @FutureOrPresent(message = "Due date must be in the future or present")
    private LocalDate dueDate;

    @NotNull(message = "Type cannot be null")
    private CreditProductType type; // Enum: PERSONAL, BUSINESS

}
