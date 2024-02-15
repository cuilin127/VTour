package com.example.vtour.configs;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<HttpToHttpsRedirectFilter> httpToHttpsRedirectFilter() {
        FilterRegistrationBean<HttpToHttpsRedirectFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HttpToHttpsRedirectFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
