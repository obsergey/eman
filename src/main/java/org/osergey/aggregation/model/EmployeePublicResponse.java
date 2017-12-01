package org.osergey.aggregation.model;

import lombok.Data;
import org.osergey.dept.model.EmployeeResponse;

@Data
public class EmployeePublicResponse {
    private String name;
    private String position;

    public EmployeePublicResponse(EmployeeResponse employee) {
        name = employee.getName();
        position = employee.getPosition();
    }
}
