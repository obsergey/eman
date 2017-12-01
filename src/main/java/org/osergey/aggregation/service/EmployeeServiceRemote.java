package org.osergey.aggregation.service;

import org.osergey.dept.domain.Employee;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.dept.model.EmployeeRequest;
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
    public EmployeeResponse findOne(int id, int dept) {
        return rest.getForObject(empOne, EmployeeResponse.class, dept, id);
    }

    @Override
    public Employee create(EmployeeRequest employee) {
        throw new NotImplementedException();
    }

    @Override
    public EmployeeResponse update(int id, int dept, EmployeeRequest employee) {
        return rest.patchForObject(empOne, employee, EmployeeResponse.class, dept, id);
    }

    @Override
    public void delete(int id, int dept) {
        throw new NotImplementedException();
    }
}
