package com.e5.ems.repository;

import com.e5.ems.model.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * It's the interface for access the Passport entity data in Database
 */
public interface PassportRepository extends JpaRepository<Passport, Integer> {

    Passport findByIdAndIsDeletedFalse(Integer passportId);
}
