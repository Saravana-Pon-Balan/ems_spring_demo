package com.e5.ems.repository;


import com.e5.ems.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * It's the interface for access the Branch entity data in Database
 */
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Branch findByIdAndIsDeletedFalse(int branchId);
}
