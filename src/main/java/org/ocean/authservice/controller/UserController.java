package org.ocean.authservice.controller;


import org.ocean.authservice.dao.UserProfileDao;
import org.ocean.authservice.entity.UserProfile;
import org.ocean.authservice.mappers.UserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserProfileMapper userProfileMapper;

    @PostMapping
    public void test(@RequestBody UserProfileDao userProfileDao) {

        UserProfile userProfile = new UserProfile();
        UserProfile entity = userProfileMapper.toEntity(userProfileDao);
        System.out.println(entity);
    }
}
