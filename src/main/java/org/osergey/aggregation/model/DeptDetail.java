package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.osergey.dept.model.Dept;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@ToString
public class DeptDetail {
    private String name;
    private String description;
    private List<EmployeePublic> employees;

    public DeptDetail(Dept dept) {
        name = dept.getName();
        description = dept.getDescription();
        if(dept.getEmployees() != null) {
            employees = dept.getEmployees().stream().map(EmployeePublic::new).collect(Collectors.toList());
        }
    }
}
