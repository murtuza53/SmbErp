/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
        http.authorizeRequests().antMatchers("/javax.faces.resource/**", "/**" /*,"/**"*/).permitAll()
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
        http.formLogin().defaultSuccessUrl("/success", true);
        //http.formLogin().successHandler(new CustomSuccessHandler());
        
        //error handling
        http.exceptionHandling().accessDeniedPage("/access.xhtml");

        // not needed as JSF 2.2 is implicitly protected against CSRF
        http.csrf().disable();
    }
    
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().
                // antMatchers("/**").
                antMatchers(PUBLIC_MATCHERS).
                permitAll().anyRequest().authenticated();

        http
                .csrf().disable().cors().disable()
                .formLogin().failureUrl("/login.xhtml?error")
                .defaultSuccessUrl("/index.xhtml")
                .loginPage("/login.xhtml").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login.xhtml").deleteCookies("remember-me").permitAll()
                .and()
                .rememberMe()
                .and()
                .sessionManagement().maximumSessions(3600)
                .and().
                invalidSessionUrl("/login.xhtml");

        //error handling
        http.exceptionHandling().accessDeniedPage("/access.xhtml");
    }*/

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }*/
}
