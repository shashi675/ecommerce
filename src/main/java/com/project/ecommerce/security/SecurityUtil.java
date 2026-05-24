package com.project.ecommerce.security;

import com.project.ecommerce.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public User getCurrentUser(){

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        return userDetails.getUser();
    }


}