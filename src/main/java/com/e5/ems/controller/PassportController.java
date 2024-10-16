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

import com.e5.ems.dto.PassportDTO;
import com.e5.ems.service.PassportService;

/**
 * <p>
 *      It's a class for get the request from client a give the response to client for manage passport
 * </p>
 */
@RestController
@RequestMapping("v1/employees/{employeeId}/passports")
public class PassportController {

    @Autowired
    private PassportService passportService;
    private final Logger logger = LogManager.getLogger(PassportController.class);

    /**
     * <p>
     *     This method is used for get the employee data from client for save.
     * </p>
     * @param employeeId
     *         is get from the client and store passport id to that employee
     * @param passportDto
     *         is get from the client and store passport in Database
     * @return {@link PassportDTO}
     *         saved Passport is returns to client
     */
    @PostMapping
    public ResponseEntity<PassportDTO> savePassport(@PathVariable int employeeId,
                                                 @RequestBody PassportDTO passportDto) {
        logger.debug("Passport({}) got from client for save", passportDto.getId());
        PassportDTO savedPassport = passportService.savePassport(employeeId, passportDto);
        if(savedPassport == null) {
            return new ResponseEntity<>(passportDto, HttpStatus.CONFLICT);
        }
        logger.info("Passport({}) saved", savedPassport.getId());
        return new ResponseEntity<>(savedPassport, HttpStatus.CREATED);
    }

    /**
     * <p>
     *     This method is used for get the employee data from client for save.
     * </p>
     * @param employeeId
     *         is get from the client for find the employee
     * @param passportId
     *         is get from the client for find the passport
     * @return PassportDTO
     *         the retrieved Passport is returns to client
     */
    @GetMapping("{passportId}")
    public ResponseEntity<PassportDTO> getPassportById(@PathVariable int passportId,
                                                       @PathVariable int employeeId) {
        logger.debug("Employee id({}) got from client for retrieve", passportId);
        PassportDTO passportDto = passportService.getPassportById(passportId);
        return new ResponseEntity<>(passportDto, HttpStatus.OK);
    }

    /**
     * <p>
     *     This method is used for get the employee data from client for update.
     * </p>
     * @param employeeId
     *         is get from the client for find the employee
     * @param passportDto
     *         is get from the client for find the passport
     * @return PassportDTO
     *         updated Passport is returns to client
     */
    @PutMapping
    public ResponseEntity<PassportDTO> updatePassport(@RequestBody PassportDTO passportDto,
                                                      @PathVariable int employeeId) {
        logger.debug("Employee({}) got from client for update", employeeId);
        PassportDTO updatedPassport = passportService.updatePassport(passportDto, employeeId);
        logger.info("Employee({}) updated and returned", employeeId);
        return new ResponseEntity<>(updatedPassport, HttpStatus.OK);
    }

    /**
     * <p>
     *     This method is used for delete the employee data from client for save.
     * </p>
     * @param employeeId
     *         is get from the client for find the employee
     * @param passportId
     *         is get from the client for find the passport
     */
    @DeleteMapping("{passportId}")
    public ResponseEntity<HttpStatus> deletePassport(@PathVariable int employeeId,
                                                     @PathVariable int passportId) {
        logger.debug("Employee id({}) got from client for delete passport", employeeId);
        passportService.deletePassport(passportId, employeeId);
        logger.info("Employee({})'s passport deleted and returned", employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
