package com.example.wantedassignment.common.config;

import com.example.wantedassignment.common.filter.CustomAuthenticationFilter;
import com.example.wantedassignment.domain.user.service.RefreshTokenService;
import com.example.wantedassignment.domain.user.service.UserService;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RefreshTokenService refreshTokenService;

    private final Environment env;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .anyRequest().permitAll()
                )
                .headers().frameOptions().disable()
                .and()
                .addFilter(getAuthenticationFilter())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutUrl("/logout")
                        .deleteCookies("refresh-token")
                        .logoutSuccessHandler((request, response, authentication) -> response.
                                setStatus(HttpServletResponse.SC_OK))
                );
    }

    private CustomAuthenticationFilter getAuthenticationFilter() throws Exception {
        return new CustomAuthenticationFilter(authenticationManager(),
                userService, refreshTokenService, env);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
    }
}
