package com.e5.ems.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e5.ems.dto.CourseDTO;
import com.e5.ems.service.CourseService;

/**
 * <p>
 *      It's a class for get the request from client a give the response to client for manage course details
 * </p>
 */
@RestController
@RequestMapping("v1/employees/{employeeId}/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    private final Logger logger = LogManager.getLogger(CourseController.class);


    /**
     * <p>
     *     This method is used for get course data from client for save.
     * </p>
     * @param employeeId
     *          is get from client for authorization
     * @param courseDto
     *          is get from the client for store course data
     * @return {@link CourseDTO}
     *         saved Course is returns to client
     */
    @PostMapping
    public ResponseEntity<CourseDTO> saveCourse(@PathVariable int employeeId,
                                                @RequestBody CourseDTO courseDto) {
        logger.debug("Employee({}) got from client for save branch", employeeId);
        CourseDTO savedCourse = courseService.saveCourse(courseDto);
        logger.info("Course({}) saved", savedCourse.getId());
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    /**
     * <p>
     *     This method is used for get course id from client for retrieve.
     * </p>
     * @param employeeId
     *          is get from client for authorization
     * @param courseId
     *          is get from the client for retrieve course data
     * @return CourseDTO
     *         retrieved Course is returns to client
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable int courseId,
                                            @PathVariable int employeeId) {
        logger.debug("Employee id({}) got from client for retrieve course", employeeId);
        CourseDTO course = courseService.getCourseById(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    /**
     * <p>
     *     This method is used for get course data from client for update.
     * </p>
     * @param employeeId
     *          is get from client for authorization
     * @param courseDto
     *          is get from the client for update course data
     * @return CourseDTO
     *         updated Course is returns to client
     */
    @PutMapping
    public ResponseEntity<CourseDTO> updateCourse(@RequestBody CourseDTO courseDto,
                                               @PathVariable int employeeId) {
        logger.debug("Employee({}) got from client for update branch", employeeId);
        CourseDTO updatedCourse = courseService.updateCourse(courseDto);
        logger.info("Employee({}) updated and returned", employeeId);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);

    }

    /**
     * <p>
     *     This method is used for get course id from client for delete.
     * </p>
     * @param employeeId
     *          is get from client for authorization
     * @param courseId
     *          is get from the client for delete course data
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable int courseId,
                                                   @PathVariable int employeeId) {
        logger.debug("Employee({}) got from client for delete course({})", employeeId, courseId);
        courseService.deleteCourse(courseId);
        logger.info("Employee({}) deleted and returned", courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("{courseId}/enroll")
    public ResponseEntity<HttpStatus> enrollCourse(@PathVariable int courseId,
                                                   @PathVariable int employeeId) {
        logger.debug("Employee({}) got from client for enroll course", employeeId);
        courseService.enrollCourse(courseId, employeeId);
        logger.info("Employee({}) enrolled and returned", employeeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
