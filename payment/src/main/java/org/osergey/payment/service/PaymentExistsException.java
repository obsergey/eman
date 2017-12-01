package org.osergey.payment.service;

public class PaymentExistsException extends RuntimeException {
    public PaymentExistsException(int id) {
        super("Payment {" + id + "} already exists");
    }
}
