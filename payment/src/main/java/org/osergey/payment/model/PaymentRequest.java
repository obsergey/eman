package org.osergey.payment.model;

import lombok.Data;

@Data
public class PaymentRequest {
    private int salary;
    private String account;
}
