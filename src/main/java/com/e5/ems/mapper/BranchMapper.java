package com.e5.ems.mapper;

import com.e5.ems.dto.BranchDTO;
import com.e5.ems.model.Branch;

public class BranchMapper {

    /**
     * <p>
     *     It is a method for converting the Branch to BranchDTO
     * </p>
     * @param branch
     *          is used for convert branchDto object to branch
     * @return BranchDTO
     *          converted branchDTO
     */
    public static BranchDTO branchToBranchDto(Branch branch) {
        return BranchDTO.builder()
                .name(branch.getName())
                .location(branch.getLocation())
                .build();
    }

    /**
     * <p>
     *     It is a method for converting the EmployeeDTO to Employee
     * </p>
     * @param branchDto
     *          is used for convert branchDto object to branch
     * @return Branch
     *          converted Branch
     */
    public static Branch branchDtoToBranch(BranchDTO branchDto) {
        return Branch.builder()
                .id(branchDto.getId())
                .name(branchDto.getName())
                .location(branchDto.getLocation())
                .build();
    }
}
