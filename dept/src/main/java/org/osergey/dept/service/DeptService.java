package org.osergey.dept.service;

import org.osergey.dept.model.DeptListPageResponse;
import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeRequest;

public interface DeptService {
    DeptListPageResponse findAll(int page, int size);
    DeptResponse findOne(int id);
    int  appendEmployee(int id, EmployeeRequest employee);
    void removeEmployee(int id, int employeeId);
}
