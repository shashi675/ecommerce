package com.project.ecommerce.security;

import com.project.ecommerce.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityUtil {

    public User getCurrentUser(){

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        log.info("Current User: {}", userDetails.getUser().getId());

        return userDetails.getUser();
    }


}