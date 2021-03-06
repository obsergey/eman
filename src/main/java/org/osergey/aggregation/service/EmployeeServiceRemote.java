package org.osergey.aggregation.service;

import org.osergey.dept.domain.Employee;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.service.DeptNotFoundException;
import org.osergey.dept.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service("remoteEmployeeService")
public class EmployeeServiceRemote implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceRemote.class);
    private static final String empOne   = "http://localhost:8081/idept/{dept}/employee/{id}";

    @Autowired
    @Qualifier("deptRest")
    private OAuth2RestOperations rest;

    private void throwNotFoundExceptionWhenNeed(HttpClientErrorException e, int id, int dept) {
        if(e.getStatusCode().value() == 404) {
            DeptNotFoundException nex = new DeptNotFoundException(id, dept);
            nex.initCause(e);
            throw nex;
        }
    }

    @Override
    public EmployeeResponse findOne(int id, int dept) {
        try {
            return rest.getForObject(empOne, EmployeeResponse.class, dept, id);
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWhenNeed(e, id, dept);
            log.error("Request error", e);
            return null;
        } catch (Exception e) {
            log.error("Request error", e);
            return null;
        }
    }

    @Override
    public Employee create(EmployeeRequest employee) {
        throw new NotImplementedException();
    }

    @Override
    public EmployeeResponse update(int id, int dept, EmployeeRequest employee) {
        try {
            return null;
            //return rest.patchForObject(empOne, employee, EmployeeResponse.class, dept, id);
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWhenNeed(e, id, dept);
            log.error("Request error", e);
            return null;
        } catch (Exception e) {
            log.error("Request error", e);
            return null;
        }
    }

    @Override
    public void delete(int id, int dept) {
        throw new NotImplementedException();
    }
}
