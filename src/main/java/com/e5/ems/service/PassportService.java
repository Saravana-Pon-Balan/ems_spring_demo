package com.e5.ems.service;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e5.ems.exception.AccessException;
import com.e5.ems.dto.PassportDTO;
import com.e5.ems.mapper.PassportMapper;
import com.e5.ems.model.Employee;
import com.e5.ems.model.Passport;
import com.e5.ems.repository.PassportRepository;

/**
 * It's the class for add business logic to manage passport details
 */
@Service
public class PassportService {

    private static final Logger logger = LogManager.getLogger(PassportService.class);
    @Autowired
    private PassportRepository passportRepository;
    @Autowired
    private EmployeeService employeeService;

    /**
     * <p>
     *     This method is used for save the passport into Database.
     * </p>
     * @param employeeId
     *          is used for find the employee in database.
     * @param passportDto
     *          is used for store that into Database
     * @return {@link PassportDTO}
     *          the saved passport is returned
     */
    public PassportDTO savePassport(int employeeId, PassportDTO passportDto) {

        Employee employee = employeeService.getEmployee(employeeId);
        if(employee.getPassport() != null) {
            return null;
        }
        Passport savedPassport = passportRepository.save(PassportMapper.passportDtoToPassport(passportDto));
        employee.setPassport(savedPassport);
        employeeService.saveEmployee(employee);
        return PassportMapper.passportToPassportDto(savedPassport);
    }

    /**
     * <p>
     *     This method is used for retrieve the passport from Database.
     * </p>
     * @param passportId
     *          is used for retrieve the specific passport by id.
     * @throws NoSuchElementException
     *          When the employee not found in database then throw an exception
     * @return PassportDTO
     *          the retrieved passport is returned
     */
    public PassportDTO getPassportById(int passportId) {
        Passport passport = passportRepository.findByIdAndIsDeletedFalse(passportId);
        if (passport == null) {
            throw new NoSuchElementException("Passport not found");
        }
        return PassportMapper.passportToPassportDto(passport);
    }

    /**
     * <p>
     *     This method is used for retrieve the employee from Database.
     * </p>
     * @param passportDataToUpdate
     *          is used for update the passport data
     * @param employeeId
     *          is used for find the employee by id.
     * @throws NoSuchElementException
     *          When the employee not found in database then throw an exception
     * @return Passport
     *          the updated passport is returned
     */
    public PassportDTO updatePassport(PassportDTO passportDataToUpdate, int employeeId) {
        Passport passport = passportRepository.findByIdAndIsDeletedFalse(passportDataToUpdate.getId());
        Employee employee = employeeService.getEmployee(employeeId);
        if (passport == null) {
            throw new NoSuchElementException("Passport not found");
        } else if(employee.getPassport().getId() != passport.getId()) {
            throw new AccessException("You don't have privilege to access other's passport");
        }
        if (passportDataToUpdate.getPlaceOfBirth() != null) {
            passport.setPlaceOfBirth(passportDataToUpdate.getPlaceOfBirth());
        }
        if (passportDataToUpdate.getPassportNumber() != null) {
            passport.setPassportNumber(passportDataToUpdate.getPassportNumber());
        }
        if (passportDataToUpdate.getDateOfExpiry() != null) {
            passport.setDateOfExpiry(passportDataToUpdate.getDateOfExpiry());
        }
        return PassportMapper.passportToPassportDto(passportRepository.save(passport));
    }

    /**
     * <p>
     *     This method is used for delete the passport from Database.
     * </p>
     * @param passportId
     *          is used for find the passport by id
     * @param employeeId
     *          is used for find the employee by id.
     * @throws NoSuchElementException
     *          When the employee not found in database then throw an exception
     */
    public void deletePassport(int passportId, int employeeId) {
        Passport passportData = passportRepository.findByIdAndIsDeletedFalse(passportId);
        Employee employee = employeeService.getEmployee(employeeId);
        if (passportData == null) {
            throw new NoSuchElementException("Passport not found");
        } else if(employee.getPassport().getId() != passportData.getId()) {
            throw new AccessException("You don't have privilege to access other's passport");
        }
        passportData.setIsDeleted(true);
        passportRepository.save(passportData);
        employee.setPassport(null);
        employeeService.saveEmployee(employee);
    }
}
