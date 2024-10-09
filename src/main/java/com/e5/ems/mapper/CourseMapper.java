package com.e5.ems.mapper;

import com.e5.ems.dto.CourseDTO;
import com.e5.ems.model.Course;


public class CourseMapper {

    /**
     * <p>
     *     It is a method for converting the Course to CourseDTO
     * </p>
     * @param course
     *          is used for convert courseDto object to course
     * @return CourseDTO
     *          converted course
     */
    public static CourseDTO courseToCourseDto(Course course) {
        return CourseDTO.builder()
                .name(course.getName())
                .description(course.getDescription())
                .build();
    }

    /**
     * <p>
     *     It is a method for converting the courseDto to course
     * </p>
     * @param courseDto
     *          is used for convert courseDto object to course
     * @return Course
     *          converted Course
     */
    public static Course courseDtoToCourse(CourseDTO courseDto) {
        return Course.builder()
                .id(courseDto.getId())
                .name(courseDto.getName())
                .description(courseDto.getDescription())
                .build();
    }
}
