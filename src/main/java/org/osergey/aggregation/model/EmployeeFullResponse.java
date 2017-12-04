package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.contact.model.ContactResponse;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.payment.model.PaymentResponse;

@Data
@NoArgsConstructor
public class EmployeeFullResponse {
    private String name;
    private String position;
    private String phone;
    private int salary;
    private String account;

    public EmployeeFullResponse(EmployeeResponse employee, ContactResponse contact, PaymentResponse payment) {
        name = contact == null ? (employee == null ? "unknown" : employee.getName()) : contact.getName();
        position = employee == null ? "unknown" : employee.getPosition();
        phone = contact == null ? "unknown" : contact.getPhone();
        salary = payment == null ? 0 : payment.getSalary();
        account = payment == null ? "unknown" :  payment.getAccount();
    }
}
