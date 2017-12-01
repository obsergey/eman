package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.model.DeptResponse;

@Data
@NoArgsConstructor
public class DeptLabelResponse {
    private String name;
    private String description;

    public DeptLabelResponse(DeptResponse dept) {
        name = dept.getName();
        description = dept.getDescription();
    }
}
