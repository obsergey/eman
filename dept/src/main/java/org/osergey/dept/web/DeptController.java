package org.osergey.dept.web;

import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.service.DeptService;
import org.osergey.dept.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/idept")
@ConditionalOnProperty("dept.micro.service")
public class DeptController {
    private static final Logger log = LoggerFactory.getLogger(DeptController.class);

    @Autowired
    @Qualifier("localDeptService")
    DeptService deptService;
    @Autowired
    @Qualifier("localEmployeeService")
    EmployeeService employeeService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<DeptResponse> findAllDept(@RequestParam("page") int page, @RequestParam("size") int size) {
        log.info("GET /idept/ page " + page + "size " + size);
        return deptService.findAll(page, size);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public DeptResponse findOneDep(@PathVariable int id) {
        log.info("GET /idept/" + id);
        return deptService.findOne(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{dept}/employee/{id}")
    public EmployeeResponse findOneEmployee(@PathVariable int id, @PathVariable int dept) {
        log.info("GET /idept/" + dept + "/employee/" + id);
        return employeeService.findOne(id, dept);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{dept}/employee")
    public void appendEmployee(@PathVariable int dept, @RequestBody EmployeeRequest employee, HttpServletResponse response) {
        log.info("POST /idept/" + dept + "/employee/ employee " + employee);
        int id = deptService.appendEmployee(dept, employee);
        response.addHeader(HttpHeaders.LOCATION, "/idept/"+dept+"/employee/"+id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/{dept}/employee/{id}")
    public EmployeeResponse updateEmployee(@PathVariable int id, @PathVariable int dept, @RequestBody EmployeeRequest employee) {
        log.info("PATH /idept/" + dept + "/employee/" + id + " employee " + employee);
        return employeeService.update(id, dept, employee);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{dept}/employee/{id}")
    public void removeEmployee(@PathVariable int id, @PathVariable int dept) {
        log.info("DELETE /dept/" + dept + "/employee/" + id);
        deptService.removeEmployee(dept, id);
    }
}
