package org.osergey.aggregation.service;

import org.osergey.dept.domain.EmployeeEntitiy;
import org.osergey.dept.model.Employee;
import org.osergey.dept.service.EmployeeService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;

@Service("remoteEmployeeService")
public class EmployeeServiceRemote implements EmployeeService {

    private static final String empOne   = "http://localhost:8081/idept/{dept}/employee/{id}";

    private RestTemplate rest = new RestTemplate();

    @PostConstruct
    public void fixPostMethod() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        rest.setRequestFactory(requestFactory);
    }

    @Override
    public Employee findOne(int id, int dept) {
        return rest.getForObject(empOne, Employee.class, dept, id);
    }

    @Override
    public EmployeeEntitiy create(Employee employee) {
        throw new NotImplementedException();
    }

    @Override
    public Employee update(int id, int dept, Employee employee) {
        return rest.patchForObject(empOne, employee, Employee.class, dept, id);
    }

    @Override
    public void delete(int id, int dept) {
        throw new NotImplementedException();
    }
}
