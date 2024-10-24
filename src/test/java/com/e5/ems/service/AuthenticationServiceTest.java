package com.e5.ems.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.e5.ems.exception.DatabaseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static java.io.FileDescriptor.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
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

    @DisplayName("load success")
    @Test
    public void testLoadUserByUsernameSuccess() {
        when(employeeRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(employee);
        assertEquals(employee.getEmail(), authenticationService.loadUserByUsername("s@gmail.com").getUsername());
    }

    @Test
    public void testLoadUserByUsernameFailure() {
        when(employeeRepository.findByEmailAndIsDeletedFalse(anyString()))
                .thenThrow(new DatabaseException("Issue in server"));
        Throwable exception = assertThrows(DatabaseException.class, () -> authenticationService.loadUserByUsername("s@gmail.com"));
        assertEquals(exception.getMessage(), "Issue in server");
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(employeeRepository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername("s@gmail.com"));
    }

    // For try
    @TestFactory
    Stream<DynamicTest> dynamicTestStream() {
        return IntStream.of(0, 2, 4, 6)
                .mapToObj(v ->
                        dynamicTest(v + " is a multiple of 2",()->assertEquals(0,v%2))
                );
    }
}
