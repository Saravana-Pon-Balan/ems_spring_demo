package com.e5.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e5.ems.model.Branch;

/**
 * It's the interface for access the Branch entity data in Database
 */
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Branch findByIdAndIsDeletedFalse(int branchId);
}
