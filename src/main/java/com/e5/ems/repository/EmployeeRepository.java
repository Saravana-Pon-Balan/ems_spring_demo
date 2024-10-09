package com.e5.ems.repository;

import com.e5.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * It's the interface for access the Employee entity data in Database
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findByIdAndIsDeletedFalse(int id);

    List<Employee> findAllByAndIsDeletedFalseOrderByIdAsc(Pageable pageable);

    Employee findByEmailAndIsDeletedFalse(String email);
}