package com.e5.ems.dto;

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
public class CourseDTO {
    private int id;
    private String name;
    private String description;
}
