package org.ocean.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "user_profile",schema = "ocean")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;
    private String job_title;
    private boolean gender;
    private LocalDate date_of_birth;
    private LocalDate joining_date;
    private LocalDate end_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_manager")
    private User lineManager;
}
