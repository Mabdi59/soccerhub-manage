package com.soccerhub.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                if (username != null) {
                    username = username.trim();
                }
                try {
                    if (username != null && !username.isEmpty()) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // No username in token (malformed) - trace log only
                        logger.trace("JWT token did not contain a subject (username)");
                    }
                } catch (UsernameNotFoundException ex) {
                    // User referenced by the token doesn't exist in DB (e.g. token forged or user deleted).
                    // Log at TRACE to avoid noisy output in normal operation.
                    logger.trace("User from JWT not found: " + username);
                }
            }
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException ex) {
            // Invalid JWT token - log at debug to avoid noisy error logs for bad tokens
            logger.debug("Invalid JWT token: " + ex.getMessage());
        } catch (Exception ex) {
            // Any other unexpected exception should be logged as error
            logger.error("Could not set user authentication in security context", ex);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
