package org.ocean.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "user_profile",schema = "ocean")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstname;
    private String lastname;
    private Date dateofbirth;
    private boolean gender;
    private Date joiningdate;
    private Date enddate;

}
