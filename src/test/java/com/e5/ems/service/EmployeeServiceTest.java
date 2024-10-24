package com.e5.ems.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.dto.LoginDTO;
import com.e5.ems.exception.AuthenticationException;
import com.e5.ems.exception.DatabaseException;
import com.e5.ems.mapper.EmployeeMapper;
import com.e5.ems.model.Employee;
import com.e5.ems.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    AuthenticationManager authenticationManager;

    private static Employee employee;
    private static EmployeeDTO employeeDto;
    private static final List<Employee> employees = new ArrayList<>();
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
        employeeDto.setName("Test Name");
        employees.add(employee);
        employees.add(employee);
        loginDto = new LoginDTO(1, "s@gmail.com", "123");
    }

    @Test
    public void testSaveEmployeeSuccess() {
        when(employeeRepository.save(employee))
                .thenReturn(employee);
        Employee result = employeeService.saveEmployee(employee);
        assertEquals(employee, result);
    }

    @Test
    public void testSaveEmployeeFailure() {
        when(employeeRepository.save(employee)).thenThrow(DatabaseException.class);
        assertThrows(DatabaseException.class,  () -> employeeService.saveEmployee(employee));
    }

    @Test
    public void testGetEmployeeSuccess() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(employee);
        Employee result = employeeService.getEmployee(1);
        assertEquals(employee, result);
    }

    @Test
    public void testGetEmployeeNotFound() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        assertThrows(NoSuchElementException.class,  () -> employeeService.getEmployee(1));
    }

    @Test
    public void testGetEmployeeFailure() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenThrow(DatabaseException.class);
        assertThrows(DatabaseException.class,  () -> employeeService.getEmployee(1));
    }
        
    @Test
    public void testGetAllEmployeesSuccess() {
        when(employeeRepository.findAllByAndIsDeletedFalseOrderByIdAsc(any(Pageable.class))).thenReturn(employees);
        assertEquals(2, employeeService.getAllEmployees(0,1).size());
    }

    @Test
    public void testGetAllEmployeesFailure() {
        when(employeeRepository.findAllByAndIsDeletedFalseOrderByIdAsc(any(Pageable.class))).thenThrow(DatabaseException.class);
        assertThrows(DatabaseException.class,  () -> employeeService.getAllEmployees(0,1));
    }

    @Test
    public void testUpdateEmployeeSuccess() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        EmployeeDTO result = employeeService.updateEmployee(employeeDto);
        assertEquals(employeeDto.getName(), result.getName());
    }

    @Test
    public void testUpdateEmployeeFailure() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenThrow(DatabaseException.class);
        assertThrows(DatabaseException.class,  () -> employeeService.updateEmployee(employeeDto));
    }

    @Test
    public void testUpdateEmployeeNotFound() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        assertThrows(NoSuchElementException.class,  () -> employeeService.updateEmployee(employeeDto));
    }

    @Test
    public void testDeleteEmployeeSuccess() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        assertTrue(employeeService.deleteEmployee(anyInt()));
    }

    @Test
    public void testDeleteEmployeeNotFound() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        assertThrows(NoSuchElementException.class,  () -> employeeService.deleteEmployee(anyInt()));
    }

    @Test
    public void testDeleteEmployeeFailure() {
        when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenThrow(DatabaseException.class);
        assertThrows(DatabaseException.class,  () -> employeeService.deleteEmployee(anyInt()));
    }

    @Test
    public void testCreateEmployeeSuccess () {
        when(employeeRepository.getEmployeeByEmail(loginDto.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        LoginDTO result = employeeService.createEmployee(loginDto);
        assertEquals(loginDto.getEmail(), result.getEmail());
    }

    @Test
    public void testCreateEmployeeDuplicate() {
        when(employeeRepository.getEmployeeByEmail(loginDto.getEmail())).thenReturn(true);
        assertThrows(DuplicateKeyException.class,  () -> employeeService.createEmployee(loginDto));
    }

    @Test
    public void testCreateEmployeeFailure() {
        when(employeeRepository.getEmployeeByEmail(loginDto.getEmail())).thenThrow(DatabaseException.class);
        assertThrows(DatabaseException.class,  () -> employeeService.createEmployee(loginDto));
    }

    @Test
    public void testCreateSessionSuccess() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(any(Authentication.class));
        assertInstanceOf(String.class, employeeService.createSession(loginDto));
    }

    @Test
    public void testCreateSessionFailure() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(AuthenticationException.class);
        assertThrows(AuthenticationException.class,  () -> employeeService.createSession(loginDto));
    }
}
