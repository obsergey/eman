package org.osergey.aggregation.service;

import org.osergey.aggregation.model.*;
import org.osergey.contact.service.ContactService;
import org.osergey.dept.service.DeptService;
import org.osergey.dept.service.EmployeeService;
import org.osergey.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AggregationServiceImpl implements AggregationService {
    @Autowired
    @Qualifier("remoteDeptService")
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
    public DeptLabelListPageResponse findAllDeptLabel(int page, int size) {
        return new DeptLabelListPageResponse(deptService.findAll(page, size));
    }

    @Override
    public DeptDetailResponse findOneDeptDetail(int id) {
        return new DeptDetailResponse(deptService.findOne(id));
    }

    @Override
    public EmployeeFullResponse findOneEmployee(int id, int dept) {
        return new EmployeeFullResponse(
                employeeService.findOne(id, dept),
                contactService.findOne(id),
                paymentService.findOne(id));
    }

    @Override
    public int appendEmployee(int dept, EmployeeFullRequest employee) {
        int id = deptService.appendEmployee(dept, employee.toEmployeeRequest());
        contactService.create(id, employee.toContactRequest());
        paymentService.create(id, employee.toPaymentRequest());
        return id;
    }

    @Override
    public EmployeeFullResponse updateEmployee(int id, int dept, EmployeeFullRequest employee) {
        return new EmployeeFullResponse(
                employeeService.update(id, dept, employee.toEmployeeRequest()),
                contactService.update(id, employee.toContactRequest()),
                paymentService.update(id, employee.toPaymentRequest()));
    }

    @Override
    public void removeEmployee(int id, int dept) {
        deptService.removeEmployee(dept, id);
        contactService.delete(id);
        paymentService.delete(id);
    }
}
