package com.dmnstage.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //@Value("#{authenticationConfiguration.authenticationManager}")
    //@Qualifier(BeanIds.AUTHENTICATION_MANAGER)
    @Autowired
    public AuthenticationManager authenticationManager;

//    @Autowired
//    private UserDetailsService customUserDetailsService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
//        security.checkTokenAccess("isAuthenticated()"); ma3endhach "ala9a
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("ClientId")
                .secret("secret")
                .authorizedGrantTypes("password")
                .scopes("user_info")
                .autoApprove(true);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager); //.userDetailsService(customUserDetailsService);

    }

}