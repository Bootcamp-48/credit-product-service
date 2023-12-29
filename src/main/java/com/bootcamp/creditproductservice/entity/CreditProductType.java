package com.bootcamp.creditproductservice.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets CreditProductType
 */
public enum CreditProductType {
    PERSONAL_CREDIT("PERSONAL_CREDIT"),

    BUSINESS_CREDIT("BUSINESS_CREDIT"),

    CREDIT_CARD("CREDIT_CARD");

    private String value;

    CreditProductType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static CreditProductType fromValue(String value) {
        for (CreditProductType b : CreditProductType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
