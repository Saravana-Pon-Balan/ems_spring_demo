package com.e5.sample.servicetest;

import com.e5.ems.dto.CourseDTO;
import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.mapper.CourseMapper;
import com.e5.ems.mapper.EmployeeMapper;
import com.e5.ems.model.Course;
import com.e5.ems.model.Employee;
import com.e5.ems.repository.CourseRepository;
import com.e5.ems.service.CourseService;
import com.e5.ems.service.EmployeeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EmployeeService employeeService;

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
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        assertEquals(courseDto, courseService.saveCourse(courseDto));
    }

    @Test
    public void testGetCourseByIdSuccess() {
        when(courseRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(course);
        assertEquals(courseDto, courseService.getCourseById(1));
    }

    @Test
    public void testGetCourseByIdIfNotFound() {
        when(courseRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        assertThrows(NoSuchElementException.class,() -> {courseService.getCourseById(1);});
    }

    @Test
    public void testUpdateCourseSuccess() {
        when(courseRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        assertEquals(courseDto, courseService.getCourseById(1));
    }

    @Test
    public void testUpdateCourseIfNotFound() {
        when(courseRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        assertThrows(NoSuchElementException.class,() -> {courseService.getCourseById(1);});
    }

    @Test
    public void testDeleteCourseSuccess() {
        Course course = mock(Course.class);
        when(courseRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(course);
        courseService.deleteCourse(1);
        verify(course).setDeleted(true);
        verify(courseRepository).save(course);
    }

    @Test
    public void testDeleteCourseIfNotFound() {
        when(courseRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        assertThrows(NoSuchElementException.class,() -> {courseService.getCourseById(1);});
    }

    @Test
    public void testEnrollCourseSuccess() {
        Course course = mock(Course.class);
        Employee employee = mock(Employee.class);
        when(courseRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(course);
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);
        when(employeeService.getEmployee(1)).thenReturn(employee);
        courseService.enrollCourse(1, 1);
        verify(employee).setCourses(List.of(course));
        verify(employeeService).saveEmployee(employee);
    }
}