package com.e5.sample.controllertest;

import com.e5.ems.controller.EmployeeController;
import com.e5.ems.dto.*;
import com.e5.ems.mapper.EmployeeMapper;
import com.e5.ems.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class) // Enable Mockito support
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    EmployeeController employeeController;

    PassportDTO passportDTO = new PassportDTO(1, "AVR", "ADSD123", new Date());
    BranchDTO branchDTO = new BranchDTO(1, "I2I", "Chennai");
    CourseDTO courseDto = new CourseDTO(1, "HTML", "It's a basic course");
    List<CourseDTO> courses = new ArrayList<>();
    LoginDTO loginDto = new LoginDTO(1, "s@gmail.com", "123");

    {
        courses.add(courseDto);
        courses.add(courseDto);
    }

    EmployeeDTO employeeDto = new EmployeeDTO(1, "saravana", createDate(2003, 8, 16), 21, "s@gmail.com", "2133",
            "dev", "AVR", passportDTO, branchDTO, courses);

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // Month is 0-based in Calendar
        return calendar.getTime();
    }

    @Test
    public void testGetEmployee() throws Exception {
        Mockito.when(employeeService.getEmployee(1)).thenReturn(EmployeeMapper.employeeDtoToEmployee(employeeDto));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("saravana")));
    }


}
