package com.e5.ems.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "passport")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "date_of_expiry")
    private Date dateOfExpiry;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

}
