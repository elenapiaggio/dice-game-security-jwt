package com.itacademy.dicesgame.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder encoder = passwordEncoder();
        UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        auth.inMemoryAuthentication()
                .withUser(users.username("admin").password("123").roles("ADMIN", "USER"))
                .withUser(users.username("elena").password("123").roles("USER"))
                .withUser("user").password("123").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().formLogin().disable()
                .authorizeRequests()
                .antMatchers("/players/").permitAll()
                .antMatchers("/players").hasRole("ADMIN")
                .antMatchers("/players/ranking").hasAnyRole("USER")
                .antMatchers("/players/**/games").hasRole("USER")
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


}
