package com.project.ecommerce.security;

import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: "
                                        + email));

//        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
//                .username(user.getName())
//                .password(user.getPassword())
//                .roles(Arrays.asList(user.getRole()))
//                .build();
        return new CustomUserDetails(user);
    }
}
