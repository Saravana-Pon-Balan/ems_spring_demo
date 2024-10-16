package com.e5.ems.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class LoginDTO {
    private int id;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String Password;
}
