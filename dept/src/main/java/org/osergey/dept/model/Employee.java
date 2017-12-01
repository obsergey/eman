package org.osergey.dept.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.osergey.dept.domain.EmployeeEntitiy;

@Data
@NoArgsConstructor
public class Employee {
    private String name;
    private String position;

    public Employee(EmployeeEntitiy employee) {
        name = employee.getName();
        position = employee.getPosition();
    }
}
