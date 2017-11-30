package org.osergey.aggregation.model;

import lombok.Data;
import lombok.ToString;
import org.osergey.dept.model.Employee;

@Data
@ToString
public class EmployeePublic {
    private String name;
    private String position;

    public EmployeePublic(Employee employee) {
        name = employee.getName();
        position = employee.getPosition();
    }
}
