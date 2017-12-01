package org.osergey.aggregation.service;

import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.service.DeptService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service("remoteDeptService")
public class DeptServiceRemote implements DeptService {

    private static final String deptPage = "http://localhost:8081/idept?page={page}&size={size}";
    private static final String deptOne  = "http://localhost:8081/idept/{id}";
    private static final String empRoot  = "http://localhost:8081/idept/{dept}/employee";
    private static final String empOne   = "http://localhost:8081/idept/{dept}/employee/{id}";

    private final RestTemplate rest = new RestTemplate();

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
    public List<DeptResponse> findAll(int page, int size) {
        return Arrays.asList(rest.getForObject(deptPage, DeptResponse[].class, page, size));
    }

    @Override
    public DeptResponse findOne(int id) {
        return rest.getForObject(deptOne, DeptResponse.class, id);
    }

    @Override
    public int appendEmployee(int id, EmployeeRequest employee) {
        return lastSegmentInt(rest.postForLocation(empRoot, employee, id));
    }

    @Override
    public void removeEmployee(int id, int employeeId) {
        rest.delete(empOne, id, employeeId);
    }
}
