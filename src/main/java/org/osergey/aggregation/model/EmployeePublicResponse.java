package org.osergey.aggregation.model;

import lombok.Data;
import org.osergey.dept.model.EmployeeResponse;

@Data
public class EmployeePublicResponse {
    private int id;
    private String name;
    private String position;

    public EmployeePublicResponse(EmployeeResponse employee) {
        id = employee.getId();
        name = employee.getName();
        position = employee.getPosition();
    }
}
