/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp;

import com.smb.erp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Burhani152
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    //@Autowired
    //private UserSecurityService userSecurityService;
    //private BCryptPasswordEncoder passwordEncoder() {
    //    return SecurityUtility.passwordEncoder();
    //}
    private static final String[] PUBLIC_MATCHERS = {
        "/javax.faces.resource/**",
        "/css/**",
        "/js/**",
        "/data/**",
        "/sound/**",
        "/img/**",
        "/",
        "/login",
        "/logout",
        "/error",
        "/index",};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // require all requests to be authenticated except for the resources
        http.authorizeRequests().antMatchers("/javax.faces.resource/**", "/viewer/**" /*,"/**"*/).permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                //.antMatchers("/jamaat/**").hasAnyAuthority("ADMIN", "JAMAAT_ADMIN")
                //.antMatchers("/famb/**").hasAnyAuthority("ADMIN", "FAMB_ADMIN")
                .anyRequest().authenticated();

        //admin
        http.authorizeRequests();

        // logout
        http.logout().clearAuthentication(true).invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.xhtml").permitAll();
        // login
        http.formLogin().loginPage("/login.xhtml").permitAll()
                .failureUrl("/login.xhtml?error=true").permitAll();

        //success
        ////http.formLogin().successForwardUrl("/index.xhtml?redirect=true");
        http.formLogin().defaultSuccessUrl("/index.xhtml", true);
        //http.formLogin().successHandler(new CustomSuccessHandler());

        //error handling
        http.exceptionHandling().accessDeniedPage("/access.xhtml");

        // not needed as JSF 2.2 is implicitly protected against CSRF
        http.csrf().disable();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
