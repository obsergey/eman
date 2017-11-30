package org.osergey.payment.service;

import org.osergey.payment.model.Payment;

public interface PaymentService {
    Payment findOne(int id);
    Payment create(int id, Payment payment);
    Payment update(int id, Payment payment);
    void delete(int id);
}
