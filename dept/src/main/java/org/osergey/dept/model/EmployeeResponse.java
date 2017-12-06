package org.osergey.dept.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.domain.Employee;

@Data
@NoArgsConstructor
public class EmployeeResponse {
    private int id;
    private String name;
    private String position;

    public EmployeeResponse(Employee employee) {
        id = employee.getId();
        name = employee.getName();
        position = employee.getPosition();
    }
}
