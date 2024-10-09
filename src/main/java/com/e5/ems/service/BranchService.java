package com.e5.ems.service;

import java.util.NoSuchElementException;

import com.e5.ems.dto.BranchDTO;
import com.e5.ems.exception.AccessException;
import com.e5.ems.mapper.BranchMapper;
import com.e5.ems.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.e5.ems.model.Branch;
import com.e5.ems.repository.BranchRepository;

/**
 * <p>
 *     It's the class for add business logic to manage the branch
 * </p>
 */
@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private EmployeeService employeeService;

    /**
     * <p>
     * This method save the branch data in Database
     * </p>
     *
     * @param branchDto
     *          it contains the branch data for store
     * @param employeeId
     *          is for authorization
     * @throws AccessException
     *          If you are not an admin
     * @return {@link BranchDTO}
     *          the saved branch
     */
    public BranchDTO saveBranch(BranchDTO branchDto, int employeeId) {
        Branch branch = BranchMapper.branchDtoToBranch(branchDto);
        if(employeeService.getEmployee(employeeId).getRole().equals("Admin")) {
            return BranchMapper.branchToBranchDto(branchRepository.save(branch));
        }
        throw new AccessException("You cannot create a branch");
    }

    /**
     * <p>
     * This method retrieve the branch data in Database
     * </p>
     * @param branchId
     *          is used for find the specific branch in Database
     * @param employeeId
     *          is used for authorization
     * @throws NoSuchElementException
     *          When the specific branch not found in Database
     * @throws AccessException
     *          If you are not associate with that branch
     * @return {@link BranchDTO}
     *          the retrieved branch
     */
    public BranchDTO getBranchById(int branchId, int employeeId) {
        if(employeeService.getEmployee(employeeId).getBranch().getId() == branchId) {
            BranchDTO branchData = BranchMapper.branchToBranchDto(branchRepository.findByIdAndIsDeletedFalse(branchId));
            if (branchData == null) {
                throw new NoSuchElementException("Branch not found");
            }
            return branchData;
        }
        throw new AccessException("You cannot get other branch details");
    }

    /**
     * <p>
     * This method update the branch data in Database
     * </p>
     * @param branch
     *          it contains the branch data for update
     * @param employeeId
     *          is used for authorization
     * @throws NoSuchElementException
     *          When the specific branch not found in Database
     * @throws AccessException
     *          If you are not an admin
     * @return BranchDTO
     *          the updated branch
     */
    public BranchDTO updateBranch(BranchDTO branchDto, int employeeId) {
        if(employeeService.getEmployee(employeeId).getRole().equals("Admin")) {
            Branch branch = BranchMapper.branchDtoToBranch(branchDto);
            Branch branchData = branchRepository.findByIdAndIsDeletedFalse(branch.getId());
            if (branchData == null) {
                throw new NoSuchElementException("Branch not found");
            }
            if(branch.getName() != null) {
                branchData.setName(branch.getName());
            }
            if(branch.getLocation() != null) {
                branchData.setLocation(branch.getLocation());
            }
            return BranchMapper.branchToBranchDto(branchRepository.save(branchData));
        }
        throw new AccessException("You cannot update a branch");
    }

    /**
     * <p>
     *      This method delete the branch data in Database
     * </p>
     * @param branchId
     *          is used for find the specific branch in Database for delete
     * @param employeeId
     *          is used for authorization
     * @throws NoSuchElementException
     *          When the specific branch not found in Database
     */
    public void deleteBranch(int branchId, int employeeId) {
        if(employeeService.getEmployee(employeeId).getRole().equals("Admin")) {
            Branch branchData = branchRepository.findByIdAndIsDeletedFalse(branchId);
            if (branchData == null) {
                throw new NoSuchElementException("Branch not found");
            }
            branchData.setDeleted(true);
            branchRepository.save(branchData);
        }
        throw new AccessException("You cannot delete a branch");

    }

    /**
     * <p>
     *     This method is used for bind with employee
     * </p>
     * @param employeeId
     *          is used for find the specific employee
     * @param branchId
     *          is used for find the specific branch
     * @throws NoSuchElementException
     *          If employee is null or branch is null
     */
    public void bindBranchToEmployee(int branchId, int employeeId) {
        Employee employee = employeeService.getEmployee(employeeId);
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(branchId);
        if (branch == null) {
            throw new NoSuchElementException("Branch not found");
        }
        employee.setBranch(branch);
        employeeService.saveEmployee(employee);
    }
}
