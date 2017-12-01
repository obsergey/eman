package org.osergey.payment.service;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(int id) {
        super("Payment {" + id + "} not found");
    }
}
