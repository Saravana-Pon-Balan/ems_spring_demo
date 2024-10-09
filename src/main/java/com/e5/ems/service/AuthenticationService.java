package com.e5.ems.service;

import com.e5.ems.model.Employee;
import com.e5.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     It is implementation for loading user data from database
 * </p>
 */
@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * <p>
     *     This method is used for load the userDetails from the Database by username of employee
     * </p>
     * @param username
     *          is used for get the employee data by username
     * @return {@link UserDetails}
     *          the retrieved userDetails is returned
     * @throws UsernameNotFoundException
     *          if the given username is not found in Database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmailAndIsDeletedFalse(username);
        if (employee == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return employee;
    }
}
