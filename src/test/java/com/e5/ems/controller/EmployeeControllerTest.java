package com.e5.ems.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.dto.LoginDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.e5.ems.mapper.EmployeeMapper;
import com.e5.ems.model.Employee;
import com.e5.ems.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;
    @Mock
    EmployeeService employeeService;

    private static Employee employee;
    private static EmployeeDTO employeeDto;
    private static final List<EmployeeDTO> employeeDtos = new ArrayList<>();
    private static LoginDTO loginDto;
    @BeforeAll
    public static void setUp() {
        employee = Employee.builder()
                .id(1)
                .name("saravana")
                .dob(new Date(16, 8, 2003))
                .mobileNumber("987213912")
                .email("s@gmail.com")
                .role("dev")
                .address("AVR")
                .build();

        employeeDto = EmployeeMapper.employeeToEmployeeDto(employee);
        employeeDtos.add(employeeDto);
        employeeDtos.add(employeeDto);
        loginDto = new LoginDTO(1, "s@gmail.com", "123");
    }

    @Test
    public void testAddEmployee() {
        when(employeeService.createEmployee(loginDto)).thenReturn(loginDto);
        ResponseEntity<LoginDTO> response = employeeController.addEmployee(loginDto);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(loginDto, response.getBody());
    }

    @Test
    public void testLogin() {
        when(employeeService.createSession(loginDto)).thenReturn(String.valueOf(String.class));
        ResponseEntity<String> response = employeeController.login(loginDto);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.valueOf(String.class), response.getBody());
    }

    @Test
    public void testGetEmployee() {
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        ResponseEntity<EmployeeDTO> response = employeeController.getEmployee(1);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDto, response.getBody());
    }

    @Test
    public void testGetAllEmployee() {
        when(employeeService.getAllEmployees(anyInt(), anyInt())).thenReturn(employeeDtos);
        ResponseEntity<List<EmployeeDTO>> response = employeeController.getAllEmployees(0,10);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdateEmployee() {
        when(employeeService.updateEmployee(employeeDto)).thenReturn(employeeDto);
        ResponseEntity<EmployeeDTO> response = employeeController.updateEmployee(employeeDto);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDto, response.getBody());
    }

    @Test
    public void testDeleteEmployee() {
        when(employeeService.deleteEmployee(anyInt())).thenReturn(true);
        ResponseEntity<HttpStatus> response = employeeController.deleteEmployee(1);
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
