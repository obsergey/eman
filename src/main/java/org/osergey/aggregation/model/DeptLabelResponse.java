package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.model.DeptResponse;

@Data
@NoArgsConstructor
public class DeptLabelResponse {
    private int id;
    private String name;
    private String description;

    public DeptLabelResponse(DeptResponse dept) {
        id = dept.getId();
        name = dept.getName();
        description = dept.getDescription();
    }
}
