package com.e5.ems.service;

import java.util.Date;

import com.e5.ems.exception.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.e5.ems.model.Employee;
import com.e5.ems.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    EmployeeRepository employeeRepository;

    private static Employee employee;
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
    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        when(employeeRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(employee);
        assertEquals(employee, authenticationService.loadUserByUsername("s@gmail.com"));
    }

    @Test
    public void testLoadUserByUsernameFailure() {
        when(employeeRepository.findByEmailAndIsDeletedFalse(anyString())).thenThrow(new DatabaseException("Issue in server"));
        assertThrows(DatabaseException.class, () -> authenticationService.loadUserByUsername("s@gmail.com"));
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(employeeRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername("s@gmail.com"));
    }

}
