package com.e5.ems.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e5.ems.dto.CourseDTO;
import com.e5.ems.mapper.CourseMapper;
import com.e5.ems.model.Employee;
import com.e5.ems.model.Course;
import com.e5.ems.repository.CourseRepository;

/**
 * It's the class for add business logic to manage course details
 */
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EmployeeService employeeService;

    /**
     * <p>
     *     This method is used for save the course from Database.
     * </p>
     * @param courseDto
     *          is used for save the course data in Database.
     * @return CourseDTO
     *          the saved course is returned
     */
    public CourseDTO saveCourse(CourseDTO courseDto) {
        Course course = CourseMapper.courseDtoToCourse(courseDto);
        return CourseMapper.courseToCourseDto(courseRepository.save(course));
    }

    /**
     * <p>
     *     This method is used for retrieve the course from Database.
     * </p>
     * @param courseId
     *          is used for retrieve the specific course by id.
     * @throws NoSuchElementException
     *          when specified course not found in Database.
     * @return Course
     *          the retrieved course is returned
     */
    public CourseDTO getCourseById(int courseId) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(courseId);
        if(course == null) {
            throw new NoSuchElementException("Course not found");
        }
        return CourseMapper.courseToCourseDto(course);
    }

    /**
     * <p>
     *     This method is used for update the course in Database.
     * </p>
     * @param courseDto
     *          is used for update the course in Database
     * @return Course
     *          the updated course is returned
     */
    public CourseDTO updateCourse(CourseDTO courseDto) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(courseDto.getId());
        if(course.getName() != null) {
            course.setName(course.getName());
        }
        if(course.getDescription() != null) {
            course.setDescription(course.getDescription());
        }
        return CourseMapper.courseToCourseDto(courseRepository.save(course));

    }

    /**
     * <p>
     *     This method is used for delete the course in Database.
     * </p>
     * @param courseId
     *          is used for find the specific course by id.
     */
    public void deleteCourse(int courseId) {
        Course courseData = courseRepository.findByIdAndIsDeletedFalse(courseId);
        courseData.setDeleted(true);
        courseRepository.save(courseData);
    }

    public void enrollCourse(int courseId, int employeeId) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(courseId);
        Employee employee = employeeService.getEmployee(employeeId);
        List<Course> courses = employee.getCourses();
        courses.add(course);
        employee.setCourses(courses);
        employeeService.saveEmployee(employee);
        System.out.println("Employee enrolled");
    }
}
