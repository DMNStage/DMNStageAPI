package com.dmnstage.api.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;
import java.util.Map;

public interface ITokenService {
    List<Map<String, String>> getAllTokensInfoByClientId(String clientId);

    Map<String, String> getTokenInfoByToken(OAuth2AccessToken token);

    Map<String, String> getTokenInfoByToken(String token);

    Map<String, String> getTokenInfoByUsername(String clientId, String username);

    Map<String, String> revokeToken();

    Map<String, String> revokeToken(String clientId, String username);

    Map<String, String> revokeToken(String token);

    Map<String, String> revokeAllTokensByClientID(String clientId);


    boolean removeAccessRefreshToken(OAuth2AccessToken accessToken) throws Exception;


}
