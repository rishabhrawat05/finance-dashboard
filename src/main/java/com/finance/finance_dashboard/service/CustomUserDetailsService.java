package com.finance.finance_dashboard.service;

import com.finance.finance_dashboard.exception.EmailNotFoundException;
import com.finance.finance_dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Here username is email of the user

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByActiveEmail(username).orElseThrow(() -> new EmailNotFoundException("User not found with email: " + username));
    }
}
