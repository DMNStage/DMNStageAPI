package com.dmnstage.api.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

//@Order(value = 100)
//@EnableOAuth2Sso
//@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //@Value("#{authenticationConfiguration.authenticationManager}")
    //@Qualifier(BeanIds.AUTHENTICATION_MANAGER)
    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Override
    public void configure(HttpSecurity http) {
        http
                .requestMatchers()
                .antMatchers("/login", "/oauth/authorize")
        ;
    }

    //    @Autowired
//    publlic void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService);
//        auth.userDetailsService(customUserDetailsService);
    }

//    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//        //return new AuthenticationManagerDelegator(authenticationBuilder);
//    }
}