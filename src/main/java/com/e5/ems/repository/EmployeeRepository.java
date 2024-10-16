package com.e5.ems.repository;

import java.util.List;

import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e5.ems.model.Employee;

/**
 * It's the interface for access the Employee entity data in Database
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findByIdAndIsDeletedFalse(int id);

    List<Employee> findAllByAndIsDeletedFalseOrderByIdAsc(Pageable pageable);

    Employee findByEmailAndIsDeletedFalse(String email);

    boolean getEmployeeByEmail(@Email String email);
}