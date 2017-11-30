package org.osergey.aggregation.service;

import org.osergey.aggregation.model.DeptDetail;
import org.osergey.aggregation.model.DeptLabel;
import org.osergey.aggregation.model.EmployeeFull;

import java.util.List;

public interface AggregationService {
    List<DeptLabel> findAllDeptLabel(int page, int size);
    DeptDetail findOneDeptDetail(int id);
    EmployeeFull findOneEmployee(int id, int dept);
    int appendEmployee(int dept, EmployeeFull employee);
    EmployeeFull updateEmployee(int id, int dept, EmployeeFull employee);
    void removeEmployee(int id, int dept);
}
