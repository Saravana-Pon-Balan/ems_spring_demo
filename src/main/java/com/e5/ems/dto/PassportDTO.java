package com.e5.ems.dto;

import java.util.Date;

import jakarta.validation.constraints.Future;
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
public class PassportDTO {
    private int id;
    @Pattern(regexp = "^[a-zA-Z]+([a-zA-Z])*$", message = "Place of birth only contains alphabets")
    private String placeOfBirth;
    @Pattern(regexp = "^[A-Z][1-9]\\\\d\\\\s?\\\\d{4}[1-9]$", message = "Name only contains alphabets")
    private String passportNumber;
    @Future(message = "your passport already expired")
    private Date dateOfExpiry;
}
