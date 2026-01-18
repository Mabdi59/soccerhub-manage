package com.soccerhub.backend.security;

import com.soccerhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Normalize incoming username to avoid mismatches due to whitespace
        String normalized = (username == null) ? null : username.trim();

        var userOpt = userRepository.findByUsername(normalized);
        if (userOpt.isEmpty() && normalized != null) {
            // Fallback to case-insensitive lookup if exact match not found
            userOpt = userRepository.findByUsernameIgnoreCase(normalized);
        }

        var user = userOpt
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return UserDetailsImpl.build(user);
    }
}
