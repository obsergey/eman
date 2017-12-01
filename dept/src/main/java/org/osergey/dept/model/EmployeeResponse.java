package org.osergey.dept.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.osergey.dept.domain.EmployeeEntitiy;

@Data
@NoArgsConstructor
public class EmployeeResponse {
    private String name;
    private String position;

    public EmployeeResponse(EmployeeEntitiy employee) {
        name = employee.getName();
        position = employee.getPosition();
    }
}
