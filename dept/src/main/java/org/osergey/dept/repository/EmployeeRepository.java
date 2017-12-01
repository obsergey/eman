package org.osergey.dept.repository;

import org.osergey.dept.domain.Employee;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty("dept.micro.service")
public interface EmployeeRepository extends CrudRepository<Employee, Integer>{
}
