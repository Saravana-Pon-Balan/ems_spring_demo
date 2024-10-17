package com.e5.ems.controller;

import java.util.Date;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.e5.ems.dto.PassportDTO;
import com.e5.ems.mapper.PassportMapper;
import com.e5.ems.model.Employee;
import com.e5.ems.model.Passport;
import com.e5.ems.service.PassportService;

@ExtendWith(MockitoExtension.class)
public class PassportControllerTest {

    @InjectMocks
    private PassportController passportController;
    @Mock
    private PassportService passportService;

    private static PassportDTO passportDto;

    @BeforeAll
    public static void setUp() {
        Employee employee = Employee.builder()
                .id(1)
                .name("saravana")
                .dob(new Date(16, 8, 2003))
                .mobileNumber("987213912")
                .email("s@gmail.com")
                .role("dev")
                .address("AVR")
                .build();
        Passport passport = Passport.builder()
                .id(1)
                .dateOfExpiry(new Date(16, 8, 2025))
                .passportNumber("ASDF123K")
                .placeOfBirth("AVR")
                .build();
        employee.setPassport(passport);
        passportDto = PassportMapper.passportToPassportDto(passport);
    }

    @Test
    public void testSavePassportSuccess() {
        when(passportService.savePassport(1, passportDto)).thenReturn(passportDto);
        ResponseEntity<PassportDTO> response = passportController.savePassport(1, passportDto);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(passportDto, response.getBody());
    }

    @Test
    public void testSavePassportFailure() {
        when(passportService.savePassport(1, passportDto)).thenReturn(null);
        ResponseEntity<PassportDTO> response = passportController.savePassport(1, passportDto);
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testGetPassport() {
        when(passportService.getPassportById(anyInt())).thenReturn(passportDto);
        ResponseEntity<PassportDTO> response = passportController.getPassportById(1,1);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passportDto, response.getBody());
    }

    @Test
    public void testUpdatePassport() {
        when(passportService.updatePassport(any(PassportDTO.class), anyInt())).thenReturn(passportDto);
        ResponseEntity<PassportDTO> response = passportController.updatePassport(passportDto, 1);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passportDto, response.getBody());
    }

    @Test
    public void testDeletePassport() {
        doNothing().when(passportService).deletePassport(1, 1);
        ResponseEntity<HttpStatus> response= passportController.deletePassport(1, 1);
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }




}
