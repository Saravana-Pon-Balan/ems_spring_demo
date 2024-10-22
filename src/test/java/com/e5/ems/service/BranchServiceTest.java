package com.e5.ems.service;

// check merge when CP

// might conflict
//checking cherry-pick
//checking cherry pick
// comment changed in branch
// amend changed
import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.e5.ems.dto.BranchDTO;
import com.e5.ems.exception.AccessException;
import com.e5.ems.mapper.BranchMapper;
import com.e5.ems.model.Branch;
import com.e5.ems.model.Employee;
import com.e5.ems.repository.BranchRepository;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    @InjectMocks
    private BranchService branchService;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private EmployeeService employeeService;

    private static Employee employee;
    private static Branch branch;
    private static Branch branch2;
    private static BranchDTO branchDto;

    @BeforeAll
    public static void setUp() {
        employee = Employee.builder()
                .id(1)
                .name("saravana")
                .dob(new Date(16, 8, 2003))
                .mobileNumber("987213912")
                .email("s@gmail.com")
                .role("dev")
                .address("AVR")
                .build();

        branch = Branch.builder()
                .id(1)
                .name("e5")
                .location("Chennai")
                .build();
        branch2 = Branch.builder()
                .id(2)
                .name("I2I")
                .location("Chennai")
                .build();
        employee.setBranch(branch);
        branchDto = BranchMapper.branchToBranchDto(branch);
    }

    @Test
    public void testSaveBranchSuccess() {
        employee.setRole("Admin");
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        assertEquals(branchDto, branchService.saveBranch(branchDto, 1));
    }

    @Test
    public void testSaveBranchFailure() {
        when(employeeService.getEmployee(1)).thenReturn(employee);
        assertThrows(AccessException.class, () -> {branchService.saveBranch(branchDto, 1);});
    }

    @Test
    public void testGetBranchByIdSuccess() {
        when(branchRepository.findByIdAndIsDeletedFalse(1)).thenReturn(branch);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        assertEquals(branchDto, branchService.getBranchById(1, 1));
    }

    @Test
    public void testGetBranchByIdIfNotFound() {
        employee.getBranch().setId(1);
        when(branchRepository.findByIdAndIsDeletedFalse(1)).thenReturn(null);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        assertThrows(NoSuchElementException.class, () -> {branchService.getBranchById(1, 1);});
    }

    @Test
    public void testGetBranchByIdWithoutAccess() {
        employee.setBranch(branch2);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        assertThrows(AccessException.class, () -> {branchService.getBranchById(1, 1);});
    }

    @Test
    public void testUpdateBranchSuccess() {
        employee.setRole("Admin");
        when(branchRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(branch);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);
        assertEquals(branchDto, branchService.updateBranch(branchDto, 1));
    }

    @Test
    public void testUpdatedBranchIfNotFound() {
        when(branchRepository.findByIdAndIsDeletedFalse(1)).thenReturn(null);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        assertThrows(NoSuchElementException.class, () -> {branchService.getBranchById(1, 1);});
    }

    @Test
    public void testUpdateBranchWithoutAccess() {
        employee.setRole("Dev");
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        assertThrows(AccessException.class, () -> {branchService.getBranchById(1, 1);});
    }

    @Test
    public void testDeleteBranchSuccess() {
        Branch branch = mock(Branch.class);
        employee.setRole("Admin");
        when(branchRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(branch);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        branchService.deleteBranch(1, 1);
        verify(branch).setDeleted(true);
        verify(branchRepository).findByIdAndIsDeletedFalse(anyInt());
        verify(branchRepository).save(branch);
    }

    @Test
    public void testDeleteBranchWithoutAccess() {
        employee.setRole("Dev");
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        assertThrows(AccessException.class, () -> {branchService.deleteBranch(1, 1);});
    }

    @Test
    public void testDeleteBranchNotFound() {
        employee.setRole("Admin");
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(branchRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> {branchService.deleteBranch(1, 1);});

    }

    @Test
    public void testBindBranchToEmployeeSuccess() {
        when(branchRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(branch);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(employeeService.saveEmployee(employee)).thenReturn(employee);
        assertEquals(true, branchService.bindBranchToEmployee(1, 1));
    }

    @Test
    public void testBindBranchToEmployeeIfNotFound() {
        when(branchRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        assertThrows(NoSuchElementException.class, () -> {branchService.bindBranchToEmployee(1, 1);});
    }

}
