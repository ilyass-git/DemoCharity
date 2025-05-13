package com.charityapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    // Liste des endpoints publics qui ne nécessitent pas d'authentification
    private final List<String> publicEndpoints = Arrays.asList(
        "/api/actions",
        "/api/categories",
        "/api/organisations",
        "/api/organisations/validees",
        "/api/auth",
        "/api/test",
        "/",
        "/login",
        "/register",
        "/home",
        "/css/",
        "/js/",
        "/images/"
    );

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Vérifier si l'URL est un endpoint public
        String requestURI = request.getRequestURI();
        logger.info("Processing request for URI: {}", requestURI);
        
        if (isPublicEndpoint(requestURI)) {
            logger.info("Public endpoint, skipping authentication");
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader("Authorization");
        logger.info("Auth header: {}", authHeader);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No valid auth header found");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Missing or invalid token\"}");
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            logger.info("Extracted JWT token: {}", jwt);
            
            final String userEmail = jwtService.extractUsername(jwt);
            logger.info("Extracted email from token: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                logger.info("Loaded user details: {}", userDetails);
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Authentication set in SecurityContext for user: {}", userEmail);
                } else {
                    logger.warn("Token is not valid for user: {}", userEmail);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid token\"}");
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error processing JWT: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
    
    private boolean isPublicEndpoint(String requestURI) {
        return publicEndpoints.stream().anyMatch(endpoint -> requestURI.startsWith(endpoint));
    }
} 