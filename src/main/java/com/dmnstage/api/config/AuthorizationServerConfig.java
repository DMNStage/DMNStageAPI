package com.dmnstage.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService customUserDetailsService;
    private final DataSource dataSource;

    @Autowired
    public AuthorizationServerConfig(PasswordEncoder passwordEncoder, UserDetailsService customUserDetailsService, DataSource dataSource, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.dataSource = dataSource;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()").passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("AdminApp")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("user_info")
                .autoApprove(true).accessTokenValiditySeconds(300).refreshTokenValiditySeconds(Integer.MAX_VALUE)
                .and().withClient("ClientApp")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("user_info")
                .autoApprove(true).accessTokenValiditySeconds(300).refreshTokenValiditySeconds(Integer.MAX_VALUE);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager).
                userDetailsService(customUserDetailsService)
                .tokenEnhancer(tokenEnhancer());
    }

}