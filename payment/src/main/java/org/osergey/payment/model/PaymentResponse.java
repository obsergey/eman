package org.osergey.payment.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.payment.domain.Payment;

@Data
@NoArgsConstructor
public class PaymentResponse {
    private int salary;
    private String account;

    public PaymentResponse(Payment payment) {
        salary = payment.getSalary();
        account = payment.getAccount();
    }
}
