package com.smb.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@SpringBootApplication
@EnableJpaAuditing
public class SmbErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmbErpApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean registerOpenSessionInViewFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
        //OpenSessionInViewFilter filter = new OpenSessionInViewFilter();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(5);
        return registrationBean;
    }

    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

}
