package org.osergey.dept.service;

public class DeptNotFoundException extends RuntimeException {
    public DeptNotFoundException(int dept) {
        super("Deportment {" + dept + "} not found");
    }
    public DeptNotFoundException(int id, int dept) {
        super("Employee {" + id + "} not found in { " + dept + " } department");
    }
}
