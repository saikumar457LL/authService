package org.ocean.authservice.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserProfileDao {
    private String username;
    private String first_name;
    private String last_name;
    private String job_title;
    private String line_manager;
    private boolean gender;
    private LocalDate date_of_birth;
    private LocalDate joining_date;
    private LocalDate end_date;
}
