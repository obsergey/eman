package org.osergey.dept.service;

import org.osergey.dept.domain.Dept;
import org.osergey.dept.domain.Employee;
import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service("localDeptService")
@ConditionalOnProperty("dept.micro.service")
@Transactional
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptRepository deptRepository;
    @Autowired
    @Qualifier("localEmployeeService")
    private EmployeeService employeeService;

    @Override
    @Transactional(readOnly = true)
    public List<DeptResponse> findAll(int page, int size) {
        return deptRepository.findAll(new PageRequest(page, size))
                .getContent().stream().map(DeptResponse::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeptResponse findOne(int id) {
        Dept entity = deptRepository.findOne(id);
        if(entity == null) {
            throw new EntityNotFoundException("Deportment {" + id + "} not found");
        }
        return DeptResponse.deep(entity);
    }

    @Override
    public int appendEmployee(int id, EmployeeRequest employee) {
        Dept entity = deptRepository.findOne(id);
        if(entity == null) {
            throw new EntityNotFoundException("Deportment {" + id + "} not found");
        }
        Employee emp = employeeService.create(employee);
        emp.setDept(entity);
        return  emp.getId();
    }

    @Override
    public void removeEmployee(int id, int employeeId) {
        employeeService.delete(employeeId, id);
    }
}
