package org.ocean.authservice.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocean.authservice.dao.UserDetailsDao;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public UserDetailsDao getLoggedInUser(){
        User userDetails = (User)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return UserDetailsDao.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    public String loggedInUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public UserDetailsDao getUserDetails(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return UserDetailsDao.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .enabled(user.isEnabled())
                .build();
    }

    public List<String> getLoggedInRoles(){
        return getLoggedInUser().getRoles();
    }

    public List<String > getRoles(String username){
        return getUserDetails(username).getRoles();
    }
}
