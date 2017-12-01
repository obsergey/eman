package org.osergey.dept.service;

import org.osergey.dept.domain.EmployeeEntitiy;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service("localEmployeeService")
@ConditionalOnProperty("dept.micro.service")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeEntitiy findChecked(int id, int dept) {
        EmployeeEntitiy entity = employeeRepository.findOne(id);
        if(entity == null || entity.getDept().getId() != dept) {
            throw new EntityNotFoundException("EmployeeResponse {" + id + "} not found in { " + dept + " } department");
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findOne(int id, int dept) {
        return new EmployeeResponse(findChecked(id, dept));
    }

    @Override
    public EmployeeEntitiy create(EmployeeRequest employee) {
        EmployeeEntitiy entity = new EmployeeEntitiy();
        entity.setName(employee.getName());
        entity.setPosition(employee.getPosition());
        return employeeRepository.save(entity);
    }

    @Override
    public EmployeeResponse update(int id, int dept, EmployeeRequest employee) {
        EmployeeEntitiy entity = findChecked(id, dept);
        entity.setName(employee.getName() != null ? employee.getName() : entity.getName());
        entity.setPosition(employee.getPosition() != null ? employee.getPosition() : entity.getPosition());
        return new EmployeeResponse(entity);
    }

    @Override
    public void delete(int id, int dept) {
        employeeRepository.delete(findChecked(id, dept));
    }
}
