package com.e5.sample.servicetest;

import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.dto.PassportDTO;
import com.e5.ems.exception.AccessException;
import com.e5.ems.mapper.EmployeeMapper;
import com.e5.ems.mapper.PassportMapper;
import com.e5.ems.model.Employee;
import com.e5.ems.model.Passport;
import com.e5.ems.repository.PassportRepository;
import com.e5.ems.service.EmployeeService;
import com.e5.ems.service.PassportService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PassportServiceTest {

    @InjectMocks
    private PassportService passportService;
    @Mock
    private PassportRepository passportRepository;
    @Mock
    private EmployeeService employeeService;

    private static Employee employee;
    private static EmployeeDTO employeeDto;
    private static Passport passport;
    private static Passport passport2;
    private static PassportDTO passportDto;

    @BeforeAll
    public static void setup() {
        employee = Employee.builder()
                .id(1)
                .name("saravana")
                .dob(new Date(16, 8, 2003))
                .mobileNumber("987213912")
                .email("s@gmail.com")
                .role("dev")
                .address("AVR")
                .build();
        passport = Passport.builder()
                .id(1)
                .dateOfExpiry(new Date(16, 8, 2025))
                .passportNumber("ASDF123K")
                .placeOfBirth("AVR")
                .build();
        passport2 = Passport.builder()
                .id(2)
                .dateOfExpiry(new Date(16, 8, 2025))
                .passportNumber("ASDF456K")
                .placeOfBirth("TEN")
                .build();
        employee.setPassport(passport);
        employeeDto = EmployeeMapper.employeeToEmployeeDto(employee);
        passportDto = PassportMapper.passportToPassportDto(passport);
    }

    @Test
    public void testSavePassportSuccess() {
        employee.setPassport(null);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(passportRepository.save(any(Passport.class))).thenReturn(passport);
        when(employeeService.saveEmployee(employee)).thenReturn(employee);
        assertEquals(passportDto, passportService.savePassport(1, passportDto));
    }

    @Test
    public void testSavePassportFailure() {
        employee.setPassport(passport);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(passportRepository.save(any(Passport.class))).thenReturn(passport);
        when(employeeService.saveEmployee(employee)).thenReturn(employee);
        assertNull(passportService.savePassport(1, passportDto));
    }

    @Test
    public void testGetPassportByIdSuccess() {
        when(passportRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(passport);
        assertEquals(passportDto, passportService.getPassportById(1));
    }

    @Test
    public void testGetPassportByIdFailure() {
        when(passportRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> passportService.getPassportById(1));
    }

    @Test
    public void testUpdatePassportSuccess() {
        employee.setPassport(passport);
        when(passportRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(passport);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(passportRepository.save(any(Passport.class))).thenReturn(passport);
        assertEquals(passportDto, passportService.updatePassport(passportDto, 1));
    }

    @Test
    public void testUpdatePassportIfNotFound() {
        when(passportRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(passportRepository.save(any(Passport.class))).thenReturn(passport);
        assertThrows(NoSuchElementException.class, () -> passportService.updatePassport(passportDto, 1));
    }

    @Test
    public void testUpdatePassportWithoutAccess() {
        employee.setPassport(passport2);
        when(passportRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(passport);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(passportRepository.save(any(Passport.class))).thenReturn(passport);
        assertThrows(AccessException.class, () -> passportService.updatePassport(passportDto, 1));
    }

    @Test
    public void testDeletePassportSuccess() {
        when(passportRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(passport);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(passportRepository.save(any(Passport.class))).thenReturn(passport);
        when(employeeService.saveEmployee(employee)).thenReturn(employee);
        passportService.deletePassport(1, 1);
        verify(employeeService, times(1)).getEmployee(1);
        verify(passportRepository, times(1)).save(passport);
        verify(employeeService, times(1)).saveEmployee(employee);
    }

    @Test
    public void testDeletePassportIfNotFound() {
        when(passportRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(passportRepository.save(any(Passport.class))).thenReturn(passport);
        when(employeeService.saveEmployee(employee)).thenReturn(employee);
        assertThrows(NoSuchElementException.class, () -> passportService.updatePassport(passportDto, 1));
    }

    @Test
    public void testDeletePassportWithoutAccess() {
        employee.setPassport(passport2);
        when(passportRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(passport);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(passportRepository.save(any(Passport.class))).thenReturn(passport);
        when(employeeService.saveEmployee(employee)).thenReturn(employee);
        assertThrows(AccessException.class, () -> passportService.updatePassport(passportDto, 1));
    }
}
