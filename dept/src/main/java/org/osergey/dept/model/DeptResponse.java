package org.osergey.dept.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.domain.DeptEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class DeptResponse {
    private String name;
    private String description;
    private List<EmployeeResponse> employees;

    public DeptResponse(DeptEntity dept) {
        name = dept.getName();
        description = dept.getDescription();
    }

    public static DeptResponse deep(DeptEntity dept) {
        DeptResponse ret = new DeptResponse(dept);
        ret.setEmployees(dept.getEmployees().stream().map(EmployeeResponse::new).collect(Collectors.toList()));
        return ret;
    }
}
