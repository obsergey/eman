package org.osergey.aggregation.model;

import lombok.Data;
import org.osergey.contact.model.Contact;
import org.osergey.dept.model.Employee;
import org.osergey.payment.model.Payment;

@Data
public class EmployeeFull {
    private String name;
    private String position;
    private String phone;
    private int salary;
    private String account;

    public EmployeeFull(Employee employee, Contact contact, Payment payment) {
        name = contact.getName();
        position = employee.getPosition();
        phone = contact.getPhone();
        salary = payment.getSalary();
        account = payment.getAccount();
    }

    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPosition(position);
        return employee;
    }

    public Contact toContact() {
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhone(phone);
        return contact;
    }

    public Payment toPayment() {
        Payment payment = new Payment();
        payment.setSalary(salary);
        payment.setAccount(account);
        return payment;
    }
}
