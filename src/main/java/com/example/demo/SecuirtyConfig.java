package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecuirtyConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationService authenticationService(){
        return new AuthenticationService();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/register", "/fetchUsers").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.formLogin()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/logged", true)
                .permitAll()
                .and()
                .logout().permitAll();

        http.csrf().disable().cors().disable();

    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(authenticationService)
                .passwordEncoder(passwordEncoder);
    }
}
