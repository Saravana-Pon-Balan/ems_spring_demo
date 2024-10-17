package com.e5.ems.controller;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.e5.ems.dto.BranchDTO;
import com.e5.ems.mapper.BranchMapper;
import com.e5.ems.model.Branch;
import com.e5.ems.model.Employee;
import com.e5.ems.service.BranchService;

@ExtendWith(MockitoExtension.class)
public class BranchControllerTest {
    @InjectMocks
    private BranchController branchController;
    @Mock
    private BranchService branchService;
    private static BranchDTO branchDto;

    @BeforeAll
    public static void setUp() {
        Employee employee = Employee.builder()
                .id(1)
                .name("saravana")
                .dob(new Date(16, 8, 2003))
                .mobileNumber("987213912")
                .email("s@gmail.com")
                .role("dev")
                .address("AVR")
                .build();
        Branch branch = Branch.builder()
                .id(1)
                .name("e5")
                .location("Chennai")
                .build();
        employee.setBranch(branch);
        branchDto = BranchMapper.branchToBranchDto(branch);
    }

    @Test
    public void testSaveBranch() {
        when(branchService.saveBranch(branchDto, 1)).thenReturn(branchDto);
        ResponseEntity<BranchDTO> response = branchController.saveBranch(1, branchDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(branchDto, response.getBody());
    }

    @Test
    public void testGetBranch() {
        when(branchService.getBranchById(1, 1)).thenReturn(branchDto);
        ResponseEntity<BranchDTO> response = branchController.getBranch(1, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(branchDto, response.getBody());
    }

    @Test
    public void testUpdateBranch() {
        when(branchService.updateBranch(branchDto, 1)).thenReturn(branchDto);
        ResponseEntity<BranchDTO> response = branchController.updateBranch(branchDto, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(branchDto, response.getBody());
    }

    @Test
    public void testDeleteBranch() {
        doNothing().when(branchService).deleteBranch(1, 1);
        ResponseEntity<HttpStatus> response= branchController.deleteBranch(1, 1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testBindBranchToEmployee() {
        when(branchService.bindBranchToEmployee(1, 1)).thenReturn(true);
        ResponseEntity<HttpStatus> response = branchController.bindBranchToEmployee(1, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
