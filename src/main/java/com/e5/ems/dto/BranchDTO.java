package com.e5.ems.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Builder
@Component
@Data
@NoArgsConstructor
public class BranchDTO {
    private int id;
    @Pattern(regexp = "^[a-zA-Z]+([a-zA-Z])*$", message = "Name only contains alphabets")
    private String name;
    @Pattern(regexp = "^[a-zA-Z]+([a-zA-Z])*$", message = "Location only contains alphabets")
    private String location;
}
