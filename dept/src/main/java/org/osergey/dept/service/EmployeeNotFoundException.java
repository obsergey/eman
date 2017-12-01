package org.osergey.dept.service;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(int id, int dept) {
        super("Employee {" + id + "} not found in { " + dept + " } department");
    }
}
