package com.e5.ems.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.e5.ems.mapper.EmployeeMapper;
import com.e5.ems.model.Employee;
import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.dto.LoginDTO;
import com.e5.ems.service.EmployeeService;
/**
 * <p>
 *      It's a class for get the request from client a give the response to client for manage employee
 * </p>
 */
@RestController
@RequestMapping("v1/employees")
public class EmployeeController {

    private final Logger logger = LogManager.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * <p>
     *     This method is used for create a new employee in Database
     * </p>
     * @param loginDto
     *          is used for get the userName and password from the client and store it in Database
     * @return {@link ResponseEntity}
     *          the Acknowledgement for created the new employee
     */
    @PostMapping("/register")
    public ResponseEntity<String> addEmployee(@RequestBody LoginDTO loginDto) {
        employeeService.createEmployee(loginDto);
        return ResponseEntity.ok("Employee registered successfully!");
    }

    /**
     * <p>
     *     This method is used for get the username and password from the client for authentication
     * </p>
     * @param loginDto
     *          is used for get the userName and password from the client for authentication
     * @return {@link ResponseEntity}
     *          the generated JWT token to the user
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDto) {
        String token = employeeService.createSession(loginDto);
        return ResponseEntity.ok(token);
    }

    /**
     * <p>
     *     This method is used for get the employee id from client for retrieve.
     * </p>
     * @param id
     *          used for get the employee by id from the Database
     * @return {@link EmployeeDTO}
     *          The retrieved employee is sent to client if employee found otherwise not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable int id) {
        logger.debug("Employee id({}) got from client for retrieve", id);
        Employee employee = employeeService.getEmployee(id);
        EmployeeDTO employeeDTO = EmployeeMapper.employeeToEmployeeDto(employee);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    /**
     * <p>
     *     This method is used for retrieve all employee from database.
     * </p>
     * @return {@link List<EmployeeDTO> } all the Employees
     *          is retrieved all employees are sent to client if employee found otherwise not found
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@RequestParam(defaultValue = "0") int pageNumber,
                                                             @RequestParam(defaultValue = "10") int pageSize) {
        logger.debug("Get all employee request from client for retrieve all");
        return new ResponseEntity<>(employeeService.getAllEmployees(pageNumber, pageSize), HttpStatus.OK);
    }

    /**
     * <p>
     *     This method is used for get the employee data from client for update.
     * </p>
     * @param employeeDto
     *          is used for update the employee data on Database
     * @return {@link ResponseEntity<EmployeeDTO>}
     *          the updated employee is return to client if employee found otherwise not found
     */
    @PutMapping
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDto) {
        logger.debug("EmployeeDto({}) got from client for update", employeeDto.getId());
        return new ResponseEntity<>(employeeService.updateEmployee(employeeDto), HttpStatus.OK);
    }

    /**
     * <p>
     *     This method is used for get the employee id from client for delete.
     * </p>
     * @param id
     *          is used for find the specific employee in database for delete
     * @return {@link ResponseEntity}
     *          If employee deleted then return status code 204 otherwise 404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable int id) {
        logger.debug("Employee id({}) got from client for delete", id);
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
