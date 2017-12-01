package org.osergey.dept.model;

import lombok.Data;

import java.util.List;

@Data
public class DeptListPageResponse {
    PaginationResponse pagination;
    List<DeptResponse> depts;
}
