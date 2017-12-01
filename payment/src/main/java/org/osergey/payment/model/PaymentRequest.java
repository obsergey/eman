package org.osergey.payment.model;

import lombok.Data;

@Data
public class PaymentRequest {
    private Integer salary;
    private String account;
}
