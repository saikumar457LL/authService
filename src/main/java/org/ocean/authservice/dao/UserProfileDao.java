package org.ocean.authservice.dao;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class UserProfileDao {
    private String username;
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private boolean gender;
    private LocalDate joining_date;
    private String job_title;
    private String line_manager;
}
