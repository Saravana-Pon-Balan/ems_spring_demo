package com.e5.sample.controllertest;

import com.e5.ems.controller.CourseController;
import com.e5.ems.dto.CourseDTO;
import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.mapper.CourseMapper;
import com.e5.ems.mapper.EmployeeMapper;
import com.e5.ems.model.Course;
import com.e5.ems.model.Employee;
import com.e5.ems.service.CourseService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;
    @Mock
    private CourseService courseService;

    private static Employee employee;
    private static EmployeeDTO employeeDto;
    private static Course course;
    private static CourseDTO courseDto;

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
        course = Course.builder()
                .id(1)
                .name("HTML")
                .description("Basic course")
                .build();
        employee.setCourses(List.of(course));
        employeeDto = EmployeeMapper.employeeToEmployeeDto(employee);
        courseDto = CourseMapper.courseToCourseDto(course);
    }

    @Test
    public void testSaveCourse() {
        when(courseService.saveCourse(any(CourseDTO.class))).thenReturn(courseDto);
        ResponseEntity<CourseDTO> response = courseController.saveCourse(1, courseDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(courseDto, response.getBody());
    }

    @Test
    public void testGetCourse() {
        when(courseService.getCourseById(anyInt())).thenReturn(courseDto);
        ResponseEntity<CourseDTO> response = courseController.getCourse(1, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseDto, response.getBody());
    }

    @Test
    public void testUpdateCourse() {
        when(courseService.updateCourse(any(CourseDTO.class))).thenReturn(courseDto);
        ResponseEntity<CourseDTO> response = courseController.updateCourse(courseDto, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseDto, response.getBody());
    }

    @Test
    public void testDeleteCourse() {
        doNothing().when(courseService).deleteCourse(anyInt());
        ResponseEntity<HttpStatus> response = courseController.deleteCourse(1, 1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void testEnrollCourse() {
        doNothing().when(courseService).enrollCourse(anyInt(), anyInt());
        ResponseEntity<HttpStatus> response = courseController.enrollCourse(1, 1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
