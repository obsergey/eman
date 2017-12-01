package org.osergey.dept.service;

import org.osergey.dept.domain.Dept;
import org.osergey.dept.domain.Employee;
import org.osergey.dept.model.DeptListPageResponse;
import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.model.PaginationResponse;
import org.osergey.dept.repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service("localDeptService")
@ConditionalOnProperty("dept.micro.service")
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptRepository deptRepository;
    @Autowired
    @Qualifier("localEmployeeService")
    private EmployeeService employeeService;

    private Dept findChecked(int id) {
        Dept entity = deptRepository.findOne(id);
        if(entity == null) {
            throw new DeptNotFoundException(id);
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public DeptListPageResponse findAll(int page, int size) {
        DeptListPageResponse ret = new DeptListPageResponse();
        ret.setPagination(new PaginationResponse(deptRepository.count(), page, size));
        ret.setDepts(deptRepository.findAll(new PageRequest(page, size))
                .getContent().stream().map(DeptResponse::new).collect(Collectors.toList()));
        return ret;
    }

    @Override
    @Transactional(readOnly = true)
    public DeptResponse findOne(int id) {
        return DeptResponse.deep(findChecked(id));
    }

    @Override
    @Transactional
    public int appendEmployee(int id, EmployeeRequest employee) {
        Dept dept = findChecked(id);
        Employee emp = employeeService.create(employee);
        emp.setDept(dept);
        return emp.getId();
    }

    @Override
    @Transactional
    public void removeEmployee(int id, int employeeId) {
        employeeService.delete(employeeId, id);
    }
}
