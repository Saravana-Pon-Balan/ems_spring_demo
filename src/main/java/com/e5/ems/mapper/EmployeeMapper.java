package com.e5.ems.mapper;

import com.e5.ems.dto.EmployeeDTO;
import com.e5.ems.dto.LoginDTO;
import com.e5.ems.model.Employee;
import com.e5.ems.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * It's the class for Mapping the employee and employeeDto
 */
@Component
public class EmployeeMapper {

    private static final Date today = new Date();

    /**
     * <p>
     *     It is a method for converting the Employee to EmployeeDTO
     * </p>
     * @param employee
     *          is used for convert employee object to employeeDto
     * @return EmployeeDTO
     *          converted employeeDTO
     */
    public static EmployeeDTO employeeToEmployeeDto(Employee employee) {
        return EmployeeDTO.builder()
                .name(employee.getName())
                .dob(employee.getDob())
                .age(DateUtil.findDifferenceOfDates(employee.getDob(), today))
                .mobileNumber(employee.getMobileNumber())
                .role(employee.getRole())
                .address(employee.getAddress())
                .passport(employee.getPassport() != null ?
                        PassportMapper.passportToPassportDto(employee.getPassport()) : null)
                .branch(employee.getBranch() != null ?
                        BranchMapper.branchToBranchDto(employee.getBranch()) : null)
                .courses(employee.getCourses().stream().map(CourseMapper::courseToCourseDto).collect(Collectors.toList()))
                .build();
    }

    /**
     * <p>
     *     It is a method for converting the Employee to EmployeeDTO for all employee retrieving
     * </p>
     * @param employee
     *          is used for convert employeeDto object to employee
     * @return EmployeeDTO
     *          converted employee
     */
    public static EmployeeDTO employeeToAllEmployeeDto(Employee employee) {
        return EmployeeDTO.builder()
                .name(employee.getName())
                .dob(employee.getDob())
                .age(DateUtil.findDifferenceOfDates(employee.getDob(), today))
                .mobileNumber(employee.getMobileNumber())
                .role(employee.getRole())
                .address(employee.getAddress())
                .build();
    }

    /**
     * <p>
     *     It is a method for converting the LoginDTO to Employee
     * </p>
     * @param loginDto
     *          is used for convert employeeDto object to employee
     * @return {@link Employee}
     *          converted employee
     */
    public static Employee loginDtoToEmployee(LoginDTO loginDto) {
        return Employee.builder()
                .email(loginDto.getEmail())
                .password(loginDto.getPassword())
                .build();
    }
}
