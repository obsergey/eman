package org.osergey.aggregation.service;

import org.osergey.dept.model.DeptListPageResponse;
import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.service.DeptNotFoundException;
import org.osergey.dept.service.DeptService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service("remoteDeptService")
public class DeptServiceRemote implements DeptService {

    private static final String deptPage = "http://localhost:8081/idept?page={page}&size={size}";
    private static final String deptOne  = "http://localhost:8081/idept/{id}";
    private static final String empRoot  = "http://localhost:8081/idept/{dept}/employee";
    private static final String empOne   = "http://localhost:8081/idept/{dept}/employee/{id}";

    private final RestTemplate rest = new RestTemplate();

    private RuntimeException wrapNotFoundException(HttpClientErrorException e, int id) {
        if(e.getStatusCode().value() == 404) {
            DeptNotFoundException nex = new DeptNotFoundException(id);
            nex.initCause(e);
            return nex;
        }
        return e;
    }

    @PostConstruct
    public void fixPostMethod() {
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    private int lastSegmentInt(URI uri) {
        String path = uri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);
        return Integer.parseInt(idStr);
    }

    @Override
    public DeptListPageResponse findAll(int page, int size) {
        return rest.getForObject(deptPage, DeptListPageResponse.class, page, size);
    }

    @Override
    public DeptResponse findOne(int id) {
        try {
            return rest.getForObject(deptOne, DeptResponse.class, id);
        } catch (HttpClientErrorException e) {
            throw wrapNotFoundException(e, id);
        }
    }

    @Override
    public int appendEmployee(int id, EmployeeRequest employee) {
        try {
            return lastSegmentInt(rest.postForLocation(empRoot, employee, id));
        } catch (HttpClientErrorException e) {
            throw wrapNotFoundException(e, id);
        }
    }

    @Override
    public void removeEmployee(int id, int employeeId) {
        rest.delete(empOne, id, employeeId);
    }
}
