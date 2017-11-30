package org.osergey.aggregation.service;

import org.osergey.dept.model.Dept;
import org.osergey.dept.model.Employee;
import org.osergey.dept.service.DeptService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service("remoteDeptSerice")
public class DeptServiceRemote implements DeptService {

    private static final String deptPage = "http://localhost:8081/idept?page={page}&size={size}";
    private static final String deptOne  = "http://localhost:8081/idept/{id}";
    private static final String empRoot  = "http://localhost:8081/idept/{dept}/employee";
    private static final String empOne   = "http://localhost:8081/idept/{dept}/employee/{id}";

    private RestTemplate rest = new RestTemplate();

    @PostConstruct
    public void fixPostMethod() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        rest.setRequestFactory(requestFactory);
    }

    private int lastSegmentInt(URI uri) {
        String path = uri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);
        return Integer.parseInt(idStr);
    }

    @Override
    public List<Dept> findAll(int page, int size) {
        return Arrays.asList(rest.getForObject(deptPage, Dept[].class, page, size));
    }

    @Override
    public Dept findOne(int id) {
        return rest.getForObject(deptOne, Dept.class, id);
    }

    @Override
    public int appendEmployee(int id, Employee employee) {
        return lastSegmentInt(rest.postForLocation(empRoot, employee, id));
    }

    @Override
    public void removeEmployee(int id, int employeeId) {
        rest.delete(empOne, id, employeeId);
    }
}
