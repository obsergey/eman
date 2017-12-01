package org.osergey.dept.model;

import lombok.Data;

import java.util.List;

@Data
public class DeptListPageResponse {
    private PaginationResponse pagination;
    private List<DeptResponse> depts;
}
