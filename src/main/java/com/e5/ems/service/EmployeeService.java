package com.e5.ems.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.e5.ems.exception.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.e5.ems.exception.AuthenticationException;
import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.dto.LoginDTO;
import com.e5.ems.mapper.EmployeeMapper;
import com.e5.ems.model.Employee;
import com.e5.ems.repository.EmployeeRepository;
import com.e5.ems.util.JwtUtil;

/**
 * It's the class for add business logic to employee
 */
@Service
public class EmployeeService {

    private final Logger logger = LogManager.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    /**
     * <p>
     *     This method is used for save the employee in Database
     * </p>
     * @param employee
     *          is used for save the specific employee in Database
     * @return {@link Employee}
     *          the saved employee
     */
    public Employee saveEmployee(Employee employee) {
        logger.debug("Processing for saving employee");
        try {
            Employee savedEmployee = employeeRepository.save(employee);
            logger.info("employee({}) saved in Database", savedEmployee.getId());
            return savedEmployee;
        } catch (Exception e) {
            logger.error(e);
            throw new DatabaseException("Issue in server");
        }
    }

    /**
     * <p>
     *     This method is used for retrieve the employee from Database.
     * </p>
     * @param id
     *          is used for retrieve the specific employee by id.
     * @throws NoSuchElementException
     *          When the employee not found in database then throw an exception
     * @return {@link Employee}
     *          the retrieved employee is returned
     */
    public Employee getEmployee(int id) {
        Employee employee = null;
        try {
            employee = employeeRepository.findByIdAndIsDeletedFalse(id);
            if(employee == null) {
                throw new NoSuchElementException("Employee with id " + id + " not found");
            }
        } catch (Exception e) {
            logger.error(e);
            if(e instanceof NoSuchElementException) {
                throw e;
            }
            throw new DatabaseException("Issue in server");

        }
        logger.info("Employee({}) retrieved successfully", employee.getId());
        return employee;

    }

    /**
     * <p>
     *     This method is used for retrieve the all employees from Database.
     * </p>
     * @return {@link List<EmployeeDTO>}
     *          list of EmployeeDTO the retrieved employees are returned
     */
    public List<EmployeeDTO> getAllEmployees(int pageNumber, int pageSize) {
        logger.debug("Processing for retrieving all employee ");
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        try {
            return employeeRepository.findAllByAndIsDeletedFalseOrderByIdAsc(pageable)
                    .stream()
                    .map(EmployeeMapper::employeeToAllEmployeeDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e);
            throw new DatabaseException("Issue in server");
        }
    }

    /**
     * <p>
     *     This method is used for update the employee in Database.
     * </p>
     * @param employeeDataToUpdate
     *          is use for find and update the specific employee.
     * @throws NoSuchElementException
     *          When the employee not found in database then throw an exception
     * @return {@link EmployeeDTO}
     *          the retrieved employeeDto is returned
     */
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDataToUpdate) {
        logger.debug("Processing for updating employee({}) ", employeeDataToUpdate.getId());
        Employee employee = null;
        try {
            employee = employeeRepository.findByIdAndIsDeletedFalse(employeeDataToUpdate.getId());
            if(employee == null) {
                throw new NoSuchElementException("Employee not found");
            }
        } catch (Exception e) {
            logger.error(e);
            if(e instanceof NoSuchElementException) {
                throw e;
            }
            throw new DatabaseException("Issue in server");
        }
        if (employeeDataToUpdate.getName() != null) {
            employee.setName(employeeDataToUpdate.getName());
        }
        if (employeeDataToUpdate.getDob() != null) {
            employee.setDob(employeeDataToUpdate.getDob());
        }
        if (employeeDataToUpdate.getMobileNumber() != null) {
            employee.setMobileNumber(employeeDataToUpdate.getMobileNumber());
        }
        if (employeeDataToUpdate.getRole() != null) {
            employee.setRole(employeeDataToUpdate.getRole());
        }
        if (employeeDataToUpdate.getAddress() != null) {
            employee.setAddress(employeeDataToUpdate.getAddress());
        }
        EmployeeDTO employeeDto = EmployeeMapper.employeeToEmployeeDto(saveEmployee(employee));
        logger.info("Employee({}) updated successfully", employeeDataToUpdate.getId());
        return employeeDto;
    }

    /**
     * <p>
     *      This method is used for delete the employee from Database.
     * </p>
     * @param id
     *          is used for find the specific employee by id and delete that.
     * @throws NoSuchElementException
     *          When the employee not found in database then throw an exception
     */
    public Boolean deleteEmployee(int id) {
        logger.debug("Processing for deleting employee({}) ", id);
        Employee employee = null;
        try {
            employee = employeeRepository.findByIdAndIsDeletedFalse(id);
            if(employee == null) {
                throw new NoSuchElementException("Employee not found");
            }
        } catch (Exception e) {
            logger.error(e);
            if(e instanceof NoSuchElementException) {
                throw e;
            }
            throw new DatabaseException("Issue in server");
        }
        employee.setIsDeleted(true);
        saveEmployee(employee);
        logger.info("Employee({}) deleted successfully", id);
        return true;
    }

    public LoginDTO createEmployee(LoginDTO loginDto) {
        try {
            if (employeeRepository.getEmployeeByEmail(loginDto.getEmail())) {
                logger.info("Employee email({}) already exists", loginDto.getEmail());
                throw new DuplicateKeyException("Email already exists");
            }
        } catch (Exception e) {
            logger.error(e);
            if(e instanceof DuplicateKeyException) {
                throw e;
            }
            throw new DatabaseException("Issue in server");
        }
        loginDto.setPassword(encoder.encode(loginDto.getPassword()));
        return EmployeeMapper.employeeToLoginDto(employeeRepository.save(EmployeeMapper.loginDtoToEmployee(loginDto)));
    }

    public String createSession(LoginDTO loginDto) {
        try {
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
                    );
            return JwtUtil.generateToken(loginDto.getEmail());
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Username or Password is incorrect");
        }
    }
}
