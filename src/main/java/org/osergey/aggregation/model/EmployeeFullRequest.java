package org.osergey.aggregation.model;

import lombok.Data;
import org.osergey.contact.model.ContactRequest;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.payment.model.PaymentRequest;

@Data
public class EmployeeFullRequest {
    private String name;
    private String position;
    private String phone;
    private int salary;
    private String account;

    public EmployeeRequest toEmployeeRequest() {
        EmployeeRequest employee = new EmployeeRequest();
        employee.setName(name);
        employee.setPosition(position);
        return employee;
    }

    public ContactRequest toContactRequest() {
        ContactRequest contact = new ContactRequest();
        contact.setName(name);
        contact.setPhone(phone);
        return contact;
    }

    public PaymentRequest toPaymentRequest() {
        PaymentRequest payment = new PaymentRequest();
        payment.setSalary(salary);
        payment.setAccount(account);
        return payment;
    }
}
