package org.osergey.aggregation.model;

import lombok.Data;
import org.osergey.aggregation.service.PostRequiredFieldException;
import org.osergey.contact.model.ContactRequest;
import org.osergey.contact.model.ContactResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.payment.model.PaymentRequest;
import org.osergey.payment.model.PaymentResponse;

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

    public void checkAppendRequestNotNullFields() {
        if(name == null) {
            throw new PostRequiredFieldException("name");
        }
        if(position == null) {
            throw new PostRequiredFieldException("position");
        }
        if(phone == null) {
            throw new PostRequiredFieldException("phone");
        }
        if(salary == null) {
            throw new PostRequiredFieldException("salary");
        }
        if(account == null) {
            throw new PostRequiredFieldException("account");
        }
    }

    public EmployeeRequest rollbackRequest(EmployeeResponse baseEmployee) {
        EmployeeRequest employee = new EmployeeRequest();
        if(name != null) {
            employee.setName(baseEmployee.getName());
        }
        if(position != null) {
            employee.setPosition(baseEmployee.getPosition());
        }
        return employee;
    }

    public ContactRequest rollbackRequest(ContactResponse baseContact) {
        ContactRequest contact = new ContactRequest();
        if(name != null) {
            contact.setName(baseContact.getName());
        }
        if(phone != null) {
            contact.setPhone(baseContact.getPhone());
        }
        return contact;
    }

    public PaymentRequest rollbackRequest(PaymentResponse basePayment) {
        PaymentRequest payment = new PaymentRequest();
        if(salary != null) {
            payment.setSalary(basePayment.getSalary());
        }
        if(account != null) {
            payment.setAccount(basePayment.getAccount());
        }
        return payment;
    }
}
