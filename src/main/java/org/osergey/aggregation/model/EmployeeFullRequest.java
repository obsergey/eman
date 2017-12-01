package org.osergey.aggregation.model;

import lombok.Data;
import org.osergey.contact.model.ContactRequest;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.payment.model.PaymentRequest;

import javax.validation.constraints.*;

@Data
public class EmployeeFullRequest {
    @Size(min = 4)
    private String name;
    private String position;
    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$")
    private String phone;
    @Min(100)
    @Max(1000)
    private Integer salary;
    @Size(min = 8, max = 24)
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
