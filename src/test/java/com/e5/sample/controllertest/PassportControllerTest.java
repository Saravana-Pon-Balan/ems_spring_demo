package com.e5.sample.controllertest;

import com.e5.ems.controller.PassportController;
import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.dto.PassportDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PassportControllerTest {

    @InjectMocks
    private PassportController passportController;
    @Mock
    private PassportService passportService;
    @Mock
    private EmployeeService employeeService;

    private static Employee employee;
    private static EmployeeDTO employeeDto;
    private static Passport passport;
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
        employee.setPassport(passport);
        employeeDto = EmployeeMapper.employeeToEmployeeDto(employee);
        passportDto = PassportMapper.passportToPassportDto(passport);
    }

    @Test
    public void testSavePassportSuccess() {
        when(passportService.savePassport(1, passportDto)).thenReturn(passportDto);
        ResponseEntity<PassportDTO> response = passportController.savePassport(1, passportDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(passportDto, response.getBody());
    }

    @Test
    public void testSavePassportFailure() {
        when(passportService.savePassport(1, passportDto)).thenReturn(null);
        ResponseEntity<PassportDTO> response = passportController.savePassport(1, passportDto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testGetPassport() {
        when(passportService.getPassportById(anyInt())).thenReturn(passportDto);
        ResponseEntity<PassportDTO> response = passportController.getPassportById(1,1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passportDto, response.getBody());
    }

    @Test
    public void testUpdatePassport() {
        when(passportService.updatePassport(any(PassportDTO.class), anyInt())).thenReturn(passportDto);
        ResponseEntity<PassportDTO> response = passportController.updatePassport(passportDto, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passportDto, response.getBody());
    }

    @Test
    public void testDeletePassport() {
        doNothing().when(passportService).deletePassport(1, 1);
        ResponseEntity<HttpStatus> response= passportController.deletePassport(1, 1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }




}
