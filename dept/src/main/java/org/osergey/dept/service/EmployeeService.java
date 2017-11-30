package org.osergey.dept.service;

import org.osergey.dept.domain.EmployeeEntitiy;
import org.osergey.dept.model.Employee;

public interface EmployeeService {
    Employee findOne(int id, int dept);
    EmployeeEntitiy create(Employee employee);
    Employee update(int id, int dept, Employee employee);
    void delete(int id, int dept);
}
