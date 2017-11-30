package org.osergey.dept;

import com.sun.deploy.net.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.osergey.dept.model.Dept;
import org.osergey.dept.model.Employee;
import org.osergey.dept.service.DeptService;
import org.osergey.dept.service.EmployeeService;
import org.osergey.dept.web.DeptController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class DeptControllerTest {
    final List<Dept> depts = new ArrayList<>();

    @Before
    public void initDepts() {
        Dept dep1 = new Dept();
        dep1.setName("Dep 1");
        dep1.setDescription("Dep 1 description");
        dep1.setEmployees(new ArrayList<>());
        Dept dep2 = new Dept();
        dep2.setName("Dep 2");
        dep2.setDescription("Dep 2 description");
        List<Employee> emps = new ArrayList<>();
        Employee emp = new Employee();
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
        List<Dept> page = new ArrayList<Dept>();
        page.add(depts.get(0));
        when(deptService.findAll(0, 1)).thenReturn(page);

        DeptController deptController = new DeptController();
        ReflectionTestUtils.setField(deptController, "deptService", deptService);

        List<Dept> depts = deptController.findAllDept(0, 1);
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

        Dept dept = deptController.findOneDep(1);
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

        Employee employee = deptController.findOneEmployee(1, 1);
        assertEquals("Vasa", employee.getName());
        assertEquals("Programmer", employee.getPosition());
    }

    @Test
    public void testUpdateEmployee() {
        final Employee employee = new Employee();
        employee.setName("Updated Name");
        employee.setPosition("Manager");

        EmployeeService employeeService = mock(EmployeeService.class);
        when(employeeService.update(1, 1, employee)).thenAnswer(new Answer<Employee>() {
            @Override
            public Employee answer(InvocationOnMock invocation) throws Throwable {
                Employee cur = depts.get(1).getEmployees().get(0);
                cur.setName(employee.getName());
                cur.setPosition(employee.getPosition());
                return cur;
            }
        });

        DeptController deptController = new DeptController();
        ReflectionTestUtils.setField(deptController, "employeeService", employeeService);

        Employee ret = deptController.updateEmployee(1, 1, employee);
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
