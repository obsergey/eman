package org.osergey.aggregation.service;

import org.osergey.aggregation.model.*;

public interface AggregationService {
    DeptLabelListPageResponse findAllDeptLabel(int page, int size);
    DeptDetailResponse findOneDeptDetail(int id);
    EmployeeFullResponse findOneEmployee(int id, int dept);
    int appendEmployee(int dept, EmployeeFullRequest employee);
    EmployeeFullResponse updateEmployee(int id, int dept, EmployeeFullRequest employee);
    void removeEmployee(int id, int dept);
}
