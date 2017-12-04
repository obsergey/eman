package org.osergey.aggregation.service;

import org.osergey.aggregation.model.*;
import org.osergey.contact.model.ContactResponse;
import org.osergey.contact.service.ContactService;
import org.osergey.dept.model.DeptListPageResponse;
import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.dept.service.DeptService;
import org.osergey.dept.service.EmployeeService;
import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AggregationServiceImpl implements AggregationService {
    private static final Logger log = LoggerFactory.getLogger(AggregationServiceImpl.class);

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

    private int checkAppendedEmployeeIdNotNull(int id) {
        if(id == 0) {
            throw new ServiceUnavailableException("dept");
        }
        return id;
    }

    private void checkServiceAvailable(Object response, String service) {
        if(response == null)
            throw new ServiceUnavailableException(service);
    }

    @Override
    public DeptLabelListPageResponse findAllDeptLabel(int page, int size) {
        if(page < 0 || size <= 0) {
            throw new PaginationBadArgumentsException();
        }
        DeptListPageResponse response = deptService.findAll(page, size);
        checkServiceAvailable(response, "dept");
        return new DeptLabelListPageResponse(response);
    }

    @Override
    public DeptDetailResponse findOneDeptDetail(int id) {
        DeptResponse response = deptService.findOne(id);
        checkServiceAvailable(response, "dept");
        return new DeptDetailResponse(response);
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
        employee.checkAppendRequestNotNullFields();
        int id = checkAppendedEmployeeIdNotNull(deptService.appendEmployee(dept, employee.toEmployeeRequest()));
        ContactResponse contact = contactService.create(id, employee.toContactRequest());
        PaymentResponse payment = paymentService.create(id, employee.toPaymentRequest());
        if(contact == null || payment == null)
        {
            deptService.removeEmployee(dept, id);
            if(contact != null)
                contactService.delete(id);
            if(payment != null)
                paymentService.delete(id);
            throw new ServiceUnavailableException(contact == null ? "contact" : "payment");
        }
        return id;
    }

    @Override
    public EmployeeFullResponse updateEmployee(int id, int dept, EmployeeFullRequest employee) {
        EmployeeResponse baseEmployee = employeeService.findOne(id, dept);
        ContactResponse baseContact = contactService.findOne(id);
        PaymentResponse basePayment = paymentService.findOne(id);
        checkServiceAvailable(baseEmployee,"dept");
        checkServiceAvailable(baseContact, "contact");
        checkServiceAvailable(basePayment, "payment");

        EmployeeResponse updEmployee = employeeService.update(id, dept, employee.toEmployeeRequest());
        ContactResponse  updContact  = contactService.update(id, employee.toContactRequest());
        PaymentResponse  updPayment  = paymentService.update(id, employee.toPaymentRequest());

        if(updEmployee != null && updContact != null && updPayment != null)
            return new EmployeeFullResponse(updEmployee, updContact, updPayment);

        if(updEmployee != null && employeeService.update(id, dept, employee.rollbackRequest(baseEmployee)) == null)
            log.error("Rollback employee " + id + " dept " + dept + " to " + baseEmployee + " failed");
        if(updContact != null && contactService.update(id, employee.rollbackRequest(baseContact)) == null)
            log.error("Rollback contact " + id + " to " + baseContact + " failed");
        if(updEmployee != null && paymentService.update(id, employee.rollbackRequest(basePayment)) == null)
            log.error("Rollback payment " + id + " to " + basePayment + " failed");

        checkServiceAvailable(updEmployee, "dept");
        checkServiceAvailable(updContact, "contact");
        checkServiceAvailable(updPayment, "payment");
        return null; // never use it
    }

    @Override
    public void removeEmployee(int id, int dept) {
        deptService.removeEmployee(dept, id);
        contactService.delete(id);
        paymentService.delete(id);
    }
}
