package com.example.UniTimeTableManagemend.cofig;

import com.example.UniTimeTableManagemend.models.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("api/v1/auth/**").permitAll()
                                .requestMatchers("api/v1/admin/**").hasAnyAuthority(Role.ADMIN.name())
                                .requestMatchers("api/v1/user/**").hasAnyAuthority(Role.STUDENT.name())
                                .requestMatchers("api/v1/faculty/**").hasAnyAuthority(Role.FACULTY.name())
                                .requestMatchers("api/v1/adminAndFaculty/**").hasAnyAuthority(Role.ADMIN.name(), Role.FACULTY.name())
                                .anyRequest().authenticated()
                )
                .sessionManagement(
                        manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
