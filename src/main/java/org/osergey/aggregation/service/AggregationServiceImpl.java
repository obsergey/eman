package org.osergey.aggregation.service;

import org.osergey.aggregation.model.DeptDetail;
import org.osergey.aggregation.model.DeptLabel;
import org.osergey.aggregation.model.EmployeeFull;
import org.osergey.contact.model.Contact;
import org.osergey.contact.service.ContactService;
import org.osergey.dept.model.Employee;
import org.osergey.dept.service.DeptService;
import org.osergey.dept.service.EmployeeService;
import org.osergey.payment.model.Payment;
import org.osergey.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AggregationServiceImpl implements AggregationService {
    @Autowired
    @Qualifier("remoteDeptSerice")
    DeptService deptService;
    @Autowired
    @Qualifier("remoteEmployeeService")
    EmployeeService employeeService;
    @Autowired
    @Qualifier("remoteContactService")
    ContactService contactService;
    @Autowired
    @Qualifier("remotePaymentService")
    PaymentService paymentService;

    private static EmployeeFull aggregate(Employee employee, Contact contact, Payment payment) {
        EmployeeFull full = new EmployeeFull();
        full.setName(contact.getName());
        full.setPosition(employee.getPosition());
        full.setPhone(contact.getPhone());
        full.setSalary(payment.getSalary());
        full.setAccount(payment.getAccount());
        return full;
    }

    private static Employee extractEmployee(EmployeeFull full) {
        Employee employee = new Employee();
        employee.setName(full.getName());
        employee.setPosition(full.getPosition());
        return employee;
    }

    private static Contact extractContact(EmployeeFull full) {
        Contact contact = new Contact();
        contact.setName(full.getName());
        contact.setPhone(full.getPhone());
        return contact;
    }

    private static Payment extractPayment(EmployeeFull full) {
        Payment payment = new Payment();
        payment.setSalary(full.getSalary());
        payment.setAccount(full.getAccount());
        return payment;
    }

    @Override
    public List<DeptLabel> findAllDeptLabel(int page, int size) {
        return deptService.findAll(page, size).stream().map(DeptLabel::new).collect(Collectors.toList());
    }

    @Override
    public DeptDetail findOneDeptDetail(int id) {
        return new DeptDetail(deptService.findOne(id));
    }

    @Override
    public EmployeeFull findOneEmployee(int id, int dept) {
        return aggregate(
                employeeService.findOne(id, dept),
                contactService.findOne(id),
                paymentService.findOne(id));
    }

    @Override
    public int appendEmployee(int dept, EmployeeFull employee) {
        int id = deptService.appendEmployee(dept, extractEmployee(employee));
        contactService.create(id, extractContact(employee));
        paymentService.create(id, extractPayment(employee));
        return id;
    }

    @Override
    public EmployeeFull updateEmployee(int id, int dept, EmployeeFull employee) {
        return aggregate(
                employeeService.update(id, dept, extractEmployee(employee)),
                contactService.update(id, extractContact(employee)),
                paymentService.update(id, extractPayment(employee)));
    }

    @Override
    public void removeEmployee(int id, int dept) {
        deptService.removeEmployee(dept, id);
        contactService.delete(id);
        paymentService.delete(id);
    }
}