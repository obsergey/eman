package org.osergey.dept.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.osergey.dept.domain.DeptEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Dept {
    private String name;
    private String description;
    private List<Employee> employees;

    public Dept(DeptEntity dept) {
        name = dept.getName();
        description = dept.getDescription();
    }

    public static Dept deep(DeptEntity dept) {
        Dept ret = new Dept(dept);
        ret.setEmployees(dept.getEmployees().stream().map(Employee::new).collect(Collectors.toList()));
        return ret;
    }
}
