package org.osergey.dept.service;

public class DeptNotFoundException extends RuntimeException {
    public DeptNotFoundException(int id) {
        super("Deportment {" + id + "} not found");
    }
}
