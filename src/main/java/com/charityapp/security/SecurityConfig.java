package com.charityapp.security;

import com.charityapp.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");
        
        http
            .csrf(csrf -> {
                csrf.disable();
                logger.info("CSRF protection disabled");
            })
            .cors(cors -> {
                cors.configurationSource(corsConfigurationSource());
                logger.info("CORS configuration applied");
            })
            .authorizeHttpRequests(auth -> {
                logger.info("Configuring authorization rules");
                auth
                // Routes publiques
                .requestMatchers("/api/test").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/register/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                .requestMatchers("/register-type/**").permitAll()
                .requestMatchers("/organisation/register").permitAll()
                .requestMatchers("/donateur/register").permitAll()
                .requestMatchers("/api/actions").permitAll()
                .requestMatchers("/api/actions/public/**").permitAll()
                .requestMatchers("/api/categories").permitAll()
                .requestMatchers("/api/categories/public/**").permitAll()
                .requestMatchers("/api/organisations").permitAll()
                .requestMatchers("/api/organisations/public/**").permitAll()
                .requestMatchers("/api/organisations/validees").permitAll()
                .requestMatchers("/", "/login", "/register", "/home").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                
                // Routes protégées par rôle
                .requestMatchers("/admin/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/admin/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/organisation/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .requestMatchers("/api/user/**").hasRole("USER")
                .requestMatchers("/api/utilisateurs/me").authenticated()
                
                // Autres routes
                .anyRequest().authenticated();
                logger.info("Authorization rules configured");
            })
            .formLogin(form -> {
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll();
                logger.info("Form login configured");
            })
            .logout(logout -> {
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
                logger.info("Logout configured");
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                logger.info("Session management configured to STATELESS");
            })
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> {
                exception.authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                });
            });

        logger.info("Security filter chain configuration completed");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
} 