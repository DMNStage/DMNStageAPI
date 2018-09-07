package com.dmnstage.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//    //.access("#oauth2.hasScope('write') "+"and #oauth2.clientHasRole('ROLE_ADMIN') "+"and hasRole('ADMIN')") //.hasRole("ADMIN")
//        http
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.NEVER)
//                .and()
//                    .csrf().disable()
//                    .anonymous().disable()
//                    .headers().frameOptions().disable()
//                .and()
//                    .authorizeRequests()
//                    .antMatchers(HttpMethod.GET, HttpPathStore.PING).permitAll()
//                    .antMatchers("/**").hasRole("**")
//                    .antMatchers("/getadmins").access("#oauth2.hasScope('write')")
//                    .anyRequest().authenticated()
//                .and()
//                    .exceptionHandling()
//                    .accessDeniedHandler(accessDeniedHandler)
//                    .authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                    .formLogin()
//                    .loginProcessingUrl(HttpPathStore.LOGIN)
//                    .successHandler(authenticationSuccessHandler)
//                    .failureHandler(authenticationFailureHandler)
//                    .permitAll()
//                .and()
//                    .logout()
//                    .logoutUrl(HttpPathStore.LOGOUT)
//                    .logoutSuccessUrl(HttpPathStore.LOGIN_FROM_LOGOUT)
//                    .logoutSuccessHandler(logoutSuccessHandler)
//                    .permitAll()
//        ;
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/revoke_all_tokens").permitAll()
                .antMatchers("/**").authenticated()
        ;
    }
}