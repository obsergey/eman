package org.osergey.dept.service;

import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeRequest;

import java.util.List;

public interface DeptService {
    List<DeptResponse> findAll(int page, int size);
    DeptResponse findOne(int id);
    int  appendEmployee(int id, EmployeeRequest employee);
    void removeEmployee(int id, int employeeId);
}
