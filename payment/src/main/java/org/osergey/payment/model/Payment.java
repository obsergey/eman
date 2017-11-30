package org.osergey.payment.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.osergey.payment.domain.PaymentEntity;

@Data
@NoArgsConstructor
@ToString
public class Payment {
    private int salary;
    private String account;

    public Payment(PaymentEntity payment) {
        salary = payment.getSalary();
        account = payment.getAccount();
    }
}
