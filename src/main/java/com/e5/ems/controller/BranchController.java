package com.e5.ems.controller;

import com.e5.ems.dto.BranchDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.e5.ems.model.Branch;
import com.e5.ems.service.BranchService;

/**
 * <p>
 *      It's a class for get the request from client a give the response to client for manage branch details
 * </p>
 */
@RestController
@RequestMapping("v1/employees/{employeeId}/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;
    private final Logger logger = LogManager.getLogger(BranchController.class);

    /**
     * <p>
     *     This method is used get the branch data from client for save
     * </p>
     * @param employeeId
     *          is used for authorization
     * @param branchDto
     *          the actual branch data is get from the client
     * @return BranchDTO
     *          the saved branch data is sent to client
     */
    @PostMapping
    public ResponseEntity<BranchDTO> saveBranch(@PathVariable int employeeId,
                                             @RequestBody BranchDTO branchDto) {
        logger.debug("Employee({}) got from client for save branch", employeeId);
        BranchDTO savedBranch = branchService.saveBranch(branchDto, employeeId);
        logger.info("Branch({}) saved", savedBranch.getId());
        return new ResponseEntity<>(savedBranch, HttpStatus.CREATED);
    }

    /**
     * <p>
     *     This method is used for get the branch id from the client for retrieve
     * </p>
     * @param branchId
     *          get from the client for retrieve
     * @param employeeId
     *          get from the client for authorization
     * @return BranchDTO
     *          the retrieved branch data is sent to client
     */
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchDTO> getBranch(@PathVariable int branchId,
                                            @PathVariable int employeeId) {
        logger.debug("Employee id({}) got from client for retrieve", branchId);
        return new ResponseEntity<>(branchService.getBranchById(branchId, employeeId), HttpStatus.OK);
    }

    /**
     * <p>
     *     This method get the branch data from client for update
     * </p>
     * @param employeeId
     *          is used for authorization
     * @param branchDto
     *          the actual branch data is get from the client
     * @return BranchDTO
     *          the updated branch data is sent to client
     */
    @PutMapping
    public ResponseEntity<BranchDTO> updateBranch(@RequestBody BranchDTO branchDto,
                                               @PathVariable int employeeId) {
        logger.debug("Employee({}) got from client for update branch", employeeId);
        BranchDTO updatedBranch = branchService.updateBranch(branchDto, employeeId);
        logger.info("Employee({}) updated and returned", employeeId);
        return new ResponseEntity<>(updatedBranch, HttpStatus.OK);
    }

    /**
     * <p>
     *     This method is used for get the branch id from the client for delete
     * </p>
     * @param employeeId
     *          is used for authorization
     * @param branchId
     *          is used for find the branch
     */
    @DeleteMapping("/{branchId}")
    public ResponseEntity<HttpStatus> deleteBranch(@PathVariable int branchId,
                                                   @PathVariable int employeeId) {
        logger.debug("Employee id({}) got from client for delete", branchId);
        branchService.deleteBranch(branchId, employeeId);
        logger.info("Employee({}) deleted and returned", branchId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * <p>
     *     This method is used for bind with employee
     * </p>
     * @param employeeId
     *          is used for find the specific employee
     * @param branchId
     *          is used for find the specific branch
     */
    @PatchMapping("/{branchId}")
    public ResponseEntity<HttpStatus> bindBranchToEmployee(@PathVariable int branchId,
                                                           @PathVariable int employeeId) {
        logger.debug("Employee({}) got from client for bind branch", employeeId);
        branchService.bindBranchToEmployee(branchId, employeeId);
        logger.info("Employee({}) bound and returned", employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
