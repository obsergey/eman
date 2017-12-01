package org.osergey.payment.service;

import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;

public interface PaymentService {
    PaymentResponse findOne(int id);
    PaymentResponse create(int id, PaymentRequest payment);
    PaymentResponse update(int id, PaymentRequest payment);
    void delete(int id);
}
