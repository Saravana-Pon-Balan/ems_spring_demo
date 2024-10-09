package com.e5.ems.repository;

import com.e5.ems.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * It's the interface for access the Course entity data in Database
 */
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Course findByIdAndIsDeletedFalse(int courseId);
}
