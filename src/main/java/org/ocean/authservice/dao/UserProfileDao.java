package org.ocean.authservice.dao;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserProfileDao {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private boolean gender;
    private Date joinDate;
    private String jobTitle;
    private String lineManager;
}
