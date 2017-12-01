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
        return new EmployeeFull(
                employeeService.findOne(id, dept),
                contactService.findOne(id),
                paymentService.findOne(id));
    }

    @Override
    public int appendEmployee(int dept, EmployeeFull employee) {
        int id = deptService.appendEmployee(dept, employee.toEmployee());
        contactService.create(id, employee.toContact());
        paymentService.create(id, employee.toPayment());
        return id;
    }

    @Override
    public EmployeeFull updateEmployee(int id, int dept, EmployeeFull employee) {
        return new EmployeeFull(
                employeeService.update(id, dept, employee.toEmployee()),
                contactService.update(id, employee.toContact()),
                paymentService.update(id, employee.toPayment()));
    }

    @Override
    public void removeEmployee(int id, int dept) {
        deptService.removeEmployee(dept, id);
        contactService.delete(id);
        paymentService.delete(id);
    }
}
