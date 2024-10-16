package com.e5.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e5.ems.model.Course;

/**
 * It's the interface for access the Course entity data in Database
 */
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Course findByIdAndIsDeletedFalse(int courseId);
}
