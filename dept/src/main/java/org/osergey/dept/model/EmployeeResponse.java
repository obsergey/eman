package org.osergey.dept.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.domain.Employee;

@Data
@NoArgsConstructor
public class EmployeeResponse {
    private String name;
    private String position;

    public EmployeeResponse(Employee employee) {
        name = employee.getName();
        position = employee.getPosition();
    }
}
