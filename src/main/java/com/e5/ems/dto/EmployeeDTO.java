package com.e5.ems.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Builder
@Component
@Data
@NoArgsConstructor
public class EmployeeDTO {

    private int id;
    @Pattern(regexp = "^[a-zA-Z]+([a-zA-Z])*$", message = "Name only contains alphabets")
    private String name;
    @NotNull(message = "Date of Birth can't be null")
    @Past(message = "Date should be past Dates")
    private Date dob;
    private int age;
    @Email
    private String email;
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Enter valid mobile number")
    private String mobileNumber;
    private String role;
    private String address;
    private PassportDTO passport;
    private BranchDTO branch;
    private List<CourseDTO> courses;
}
