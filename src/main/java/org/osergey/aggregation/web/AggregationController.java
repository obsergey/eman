package org.osergey.aggregation.web;

import org.osergey.aggregation.model.DeptDetail;
import org.osergey.aggregation.model.DeptLabel;
import org.osergey.aggregation.model.EmployeeFull;
import org.osergey.aggregation.service.AggregationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/dept")
public class AggregationController {
    private static final Logger log = LoggerFactory.getLogger(AggregationController.class);

    @Autowired
    private AggregationService aggregationService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<DeptLabel> findAllDeptLabel(@RequestParam("page") int page, @RequestParam("size") int size) {
        log.info("GET /dept/ page " + page + " size " + size);
        return aggregationService.findAllDeptLabel(page, size);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public DeptDetail findOneDeptDetail(@PathVariable int id) {
        log.info("GET /dept/" + id);
        return aggregationService.findOneDeptDetail(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{dept}/employee/{id}")
    public EmployeeFull findOneEmployee(@PathVariable int id, @PathVariable int dept) {
        log.info("GET /dept/" + dept + "/employee/" + id);
        return aggregationService.findOneEmployee(id, dept);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{dept}/employee")
    public void appendEmployee(@PathVariable int dept, @RequestBody EmployeeFull employee, HttpServletResponse response) {
        log.info("POST /dept/" + dept + "/employee/ employee " + employee);
        int id = aggregationService.appendEmployee(dept, employee);
        response.addHeader(HttpHeaders.LOCATION, "/dept/"+dept+"/employee/"+id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/{dept}/employee/{id}")
    public EmployeeFull updateEmployee(@PathVariable int id, @PathVariable int dept, @RequestBody EmployeeFull employee) {
        log.info("PATCH /dept/" + dept + "/employee/" + id + "employee" + employee);
        return aggregationService.updateEmployee(id, dept, employee);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{dept}/employee/{id}")
    public void removeEmployee(@PathVariable int id, @PathVariable int dept) {
        log.info("DELETE /dept/" + dept + "/employee/" + id);
        aggregationService.removeEmployee(id, dept);
    }
}