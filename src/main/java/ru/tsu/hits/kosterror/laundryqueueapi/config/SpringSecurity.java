package ru.tsu.hits.kosterror.laundryqueueapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.tsu.hits.kosterror.laundryqueueapi.security.JwtFilter;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurity {

    private static final String ADMIN = "ADMIN";
    private static final String EMPLOYEE = "EMPLOYEE";

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/api/creator/student/**").hasAnyRole(ADMIN, EMPLOYEE)
                .antMatchers(HttpMethod.POST, "/api/creator/employee/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/api/money/decrease").hasRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/api/machines").hasAnyRole(ADMIN, EMPLOYEE)
                .antMatchers(HttpMethod.DELETE, "/api/machines/*").hasAnyRole(ADMIN, EMPLOYEE)
                .antMatchers(HttpMethod.PUT, "/api/machines/*").hasAnyRole(ADMIN, EMPLOYEE)
                .antMatchers("/api/money/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
