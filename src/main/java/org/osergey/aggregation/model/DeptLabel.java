package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.osergey.dept.model.Dept;

@Data
@NoArgsConstructor
public class DeptLabel {
    private String name;
    private String description;

    public DeptLabel(Dept dept) {
        name = dept.getName();
        description = dept.getDescription();
    }
}
