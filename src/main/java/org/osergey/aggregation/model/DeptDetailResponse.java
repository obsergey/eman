package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.model.DeptResponse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class DeptDetailResponse {
    private String name;
    private String description;
    private List<EmployeePublicResponse> employees;

    public DeptDetailResponse(DeptResponse dept) {
        name = dept.getName();
        description = dept.getDescription();
        if(dept.getEmployees() != null) {
            employees = dept.getEmployees().stream().map(EmployeePublicResponse::new).collect(Collectors.toList());
        }
    }
}
