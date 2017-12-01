package org.osergey.dept.service;

import org.osergey.dept.domain.Employee;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.dept.model.EmployeeRequest;

public interface EmployeeService {
    EmployeeResponse findOne(int id, int dept);
    Employee create(EmployeeRequest employee);
    EmployeeResponse update(int id, int dept, EmployeeRequest employee);
    void delete(int id, int dept);
}
