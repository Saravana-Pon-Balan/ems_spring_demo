package com.e5.ems.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "employee")
public class Employee implements UserDetails {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;

     private String name;

     private Date dob;

     private String email;

     @Column(name = "mobile_number")
     private String mobileNumber;

     private String role;

     private String address;

     private String password;
     @Column(name = "is_deleted")
     private Boolean isDeleted = false;

     @OneToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "passport_id")
     private Passport passport;

     @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
             CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
     @JoinColumn(name = "branch_id")
     private Branch branch;

     @ManyToMany(fetch = FetchType.EAGER,
             cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                     CascadeType.DETACH, CascadeType.REFRESH})
     @JoinTable(
             name = "employee_course_mapper",
             joinColumns = {@JoinColumn(name = "employee_id")},
             inverseJoinColumns = {@JoinColumn(name = "course_id")}
     )
     private List<Course> courses;

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          return Collections.singleton(new SimpleGrantedAuthority("USER"));
     }

     @Override
     public String getUsername() {
          return this.email;
     }
}



