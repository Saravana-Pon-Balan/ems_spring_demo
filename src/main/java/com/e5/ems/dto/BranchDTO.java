package com.e5.ems.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+([a-zA-Z])*$", message = "Name only contains alphabets")
    private String name;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+([a-zA-Z])*$", message = "Location only contains alphabets")
    private String location;
}
