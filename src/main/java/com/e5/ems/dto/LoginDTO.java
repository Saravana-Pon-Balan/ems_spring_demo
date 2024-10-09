package com.e5.ems.dto;

import jakarta.validation.constraints.Email;

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
    @Email
    private String email;
    private String Password;
}
