package org.osergey.aggregation.service;

import org.osergey.dept.model.DeptListPageResponse;
import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.service.DeptNotFoundException;
import org.osergey.dept.service.DeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service("remoteDeptService")
public class DeptServiceRemote implements DeptService {
    private static final Logger log = LoggerFactory.getLogger(DeptServiceRemote.class);
    private static final String deptPage = "http://localhost:8081/idept?page={page}&size={size}";
    private static final String deptOne  = "http://localhost:8081/idept/{id}";
    private static final String empRoot  = "http://localhost:8081/idept/{dept}/employee";
    private static final String empOne   = "http://localhost:8081/idept/{dept}/employee/{id}";

    private final RestTemplate rest = new RestTemplate();

    private void throwNotFoundExceptionWnenNeed(HttpClientErrorException e, int id) {
        if(e.getStatusCode().value() == 404) {
            DeptNotFoundException nex = new DeptNotFoundException(id);
            nex.initCause(e);
            throw nex;
        }
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
        try {
           return rest.getForObject(deptPage, DeptListPageResponse.class, page, size);
        } catch (Exception e) {
            log.error("Request error", e);
            return null;
        }
    }

    @Override
    public DeptResponse findOne(int id) {
        try {
            return rest.getForObject(deptOne, DeptResponse.class, id);
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWnenNeed(e, id);
            log.error("Request error", e);
            return null;
        } catch (Exception e) {
            log.error("Request error", e);
            return null;
        }
    }

    @Override
    public int appendEmployee(int id, EmployeeRequest employee) {
        try {
            return lastSegmentInt(rest.postForLocation(empRoot, employee, id));
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWnenNeed(e, id);
            log.error("Request error", e);
            return 0;
        } catch (Exception e) {
            log.error("Request error", e);
            return 0;
        }
    }

    @Override
    public void removeEmployee(int id, int employeeId) {
        rest.delete(empOne, id, employeeId);
    }
}
