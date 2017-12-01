package org.osergey.aggregation.service;

import org.osergey.aggregation.model.DeptDetailResponse;
import org.osergey.aggregation.model.DeptLabelResponse;
import org.osergey.aggregation.model.EmployeeFullResponse;
import org.osergey.aggregation.model.EmployeeFullRequest;

import java.util.List;

public interface AggregationService {
    List<DeptLabelResponse> findAllDeptLabel(int page, int size);
    DeptDetailResponse findOneDeptDetail(int id);
    EmployeeFullResponse findOneEmployee(int id, int dept);
    int appendEmployee(int dept, EmployeeFullRequest employee);
    EmployeeFullResponse updateEmployee(int id, int dept, EmployeeFullRequest employee);
    void removeEmployee(int id, int dept);
}
