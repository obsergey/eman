package org.osergey.dept.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.osergey.dept.model.DeptResponse;
import org.osergey.dept.model.EmployeeResponse;
import org.osergey.dept.model.EmployeeRequest;
import org.osergey.dept.service.DeptService;
import org.osergey.dept.service.EmployeeService;
import org.osergey.dept.web.DeptController;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DeptControllerTest {
    private final List<DeptResponse> depts = new ArrayList<>();

    @Before
    public void initDepts() {
        DeptResponse dep1 = new DeptResponse();
        dep1.setName("Dep 1");
        dep1.setDescription("Dep 1 description");
        dep1.setEmployees(new ArrayList<>());
        DeptResponse dep2 = new DeptResponse();
        dep2.setName("Dep 2");
        dep2.setDescription("Dep 2 description");
        List<EmployeeResponse> emps = new ArrayList<>();
        EmployeeResponse emp = new EmployeeResponse();
        emp.setName("Vasa");
        emp.setPosition("Programmer");
        emps.add(emp);
        dep2.setEmployees(emps);
        depts.add(dep1);
        depts.add(dep2);
    }

    @Test
    public void findAllDept() {
        DeptService deptService = mock(DeptService.class);
        List<DeptResponse> page = new ArrayList<>();
        page.add(depts.get(0));
        when(deptService.findAll(0, 1)).thenReturn(page);

        DeptController deptController = new DeptController();
        ReflectionTestUtils.setField(deptController, "deptService", deptService);

        List<DeptResponse> depts = deptController.findAllDept(0, 1);
        assertEquals(1, depts.size());
        assertEquals("Dep 1", depts.get(0).getName());
        assertEquals("Dep 1 description", depts.get(0).getDescription());
    }

    @Test
    public void findOneDep() {
        DeptService deptService = mock(DeptService.class);
        when(deptService.findOne(1)).thenReturn(depts.get(1));

        DeptController deptController = new DeptController();
        ReflectionTestUtils.setField(deptController, "deptService", deptService);

        DeptResponse dept = deptController.findOneDep(1);
        assertEquals("Dep 2", dept.getName());
        assertEquals("Dep 2 description", dept.getDescription());
        assertEquals(1, dept.getEmployees().size());
        assertEquals("Vasa", dept.getEmployees().get(0).getName());
        assertEquals("Programmer", dept.getEmployees().get(0).getPosition());
    }

    @Test
    public void findOneEmployee() {
        EmployeeService employeeService = mock(EmployeeService.class);
        when(employeeService.findOne(1, 1)).thenReturn(depts.get(1).getEmployees().get(0));

        DeptController deptController = new DeptController();
        ReflectionTestUtils.setField(deptController, "employeeService", employeeService);

        EmployeeResponse employee = deptController.findOneEmployee(1, 1);
        assertEquals("Vasa", employee.getName());
        assertEquals("Programmer", employee.getPosition());
    }

    @Test
    public void testUpdateEmployee() {
        final EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("Updated Name");
        employeeRequest.setPosition("Manager");

        EmployeeService employeeService = mock(EmployeeService.class);
        when(employeeService.update(1, 1, employeeRequest)).thenAnswer((Answer<EmployeeResponse>) invocation -> {
            EmployeeResponse cur = depts.get(1).getEmployees().get(0);
            cur.setName(employeeRequest.getName());
            cur.setPosition(employeeRequest.getPosition());
            return cur;
        });

        DeptController deptController = new DeptController();
        ReflectionTestUtils.setField(deptController, "employeeService", employeeService);

        EmployeeResponse ret = deptController.updateEmployee(1, 1, employeeRequest);
        assertEquals("Updated Name", ret.getName());
        assertEquals("Manager", ret.getPosition());
    }

    @Test
    public void testRemoveEmployee() {
        DeptService deptService = mock(DeptService.class);
        DeptController deptController = new DeptController();
        ReflectionTestUtils.setField(deptController, "deptService", deptService);

        deptController.removeEmployee(3, 1);
        verify(deptService).removeEmployee(1, 3);

    }
}
