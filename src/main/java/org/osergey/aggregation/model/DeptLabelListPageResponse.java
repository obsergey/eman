package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.model.DeptListPageResponse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class DeptLabelListPageResponse {
    AggPaginationResponse pagination;
    List<DeptLabelResponse> deptLabels;

    public DeptLabelListPageResponse(DeptListPageResponse response) {
        pagination = new AggPaginationResponse(response.getPagination());
        deptLabels = response.getDepts().stream().map(DeptLabelResponse::new).collect(Collectors.toList());
    }
}
