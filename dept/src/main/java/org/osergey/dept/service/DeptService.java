package org.osergey.dept.service;

import org.osergey.dept.model.Dept;
import org.osergey.dept.model.Employee;

import java.util.List;

public interface DeptService {
    List<Dept> findAll(int page, int size);
    Dept findOne(int id);
    int  appendEmployee(int id, Employee employee);
    void removeEmployee(int id, int employeeId);
}
