package com.example.approval.service;

import com.example.approval.entity.User;
import com.example.approval.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.emptyList() // Simplification: no roles mapping yet
        );
    }

    public User createBcryptUser(String username, String password, String fullName, String role, String encoder) {
        // Not used in production typically, just for setup
        return null;
    }
}
