package com.dmnstage.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class ITokenServiceImp implements ITokenService {
    private final TokenStore tokenStore;

    @Autowired
    public ITokenServiceImp(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }


    //V1 ila chi client texpira lih laccess token o dar chi requete bih o tl3o lih expired moraha la3aytna 3la had lfonction maghadich tjbdo
    // 3ilman anaho mzl 3ando refresh token o mzzl i9ad yakhod access token akhor
    @Override
    public List<Map<String, String>> getAllTokensInfoByClientId(String clientId) {
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);
        System.out.println(tokens);
        List<Map<String, String>> tokensInfo = new ArrayList<>();

        if (tokens != null) {
            for (OAuth2AccessToken token : tokens) {
                tokensInfo.add(getTokenInfoByToken(token));
            }
        }
        return tokensInfo;
    }

    /* @Override
     public List<Map<String, String>> getAllTokensInfoByClientId(String clientId)
     {
         List<Client> clients=service.getAllClients();
         List<Map<String,String>> tokensInfo=new ArrayList<>();
         if (clients != null) {
             for (Client client : clients) {
                 Map<String, String> map =getTokenInfoByUsername(clientId,client.getUsername());
                 System.out.println(map);
                 if (map.get("result").equalsIgnoreCase("ok") || map.get("result").equalsIgnoreCase("expired"))
                 {
                     map.remove("result");
                     map.put("email",client.getEmail());
                     tokensInfo.add(map);
                 }
             }
         }
         return tokensInfo;
     }*/
    @Override
    public Map<String, String> getTokenInfoByToken(OAuth2AccessToken token) {
        if (token == null)
            return getTokenInfoByToken((String) null);
        else
            return getTokenInfoByToken(token.getValue());
    }

    @Override
    public Map<String, String> getTokenInfoByToken(String token) {

        Map<String, String> map = new HashMap<>();
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);

        if (oAuth2AccessToken == null) {
            map.put("result", "notfound");
        } else if (oAuth2AccessToken.isExpired()) {
            map.put("result", "expired");
            map.put("expireIn", String.valueOf(oAuth2AccessToken.getExpiresIn()));
            map.put("username", oAuth2AccessToken.getAdditionalInformation().get("username").toString());
            map.put("accesstoken", oAuth2AccessToken.getValue());
            map.put("active", getActiveDuration(new Timestamp(oAuth2AccessToken.getExpiration().getTime()).toLocalDateTime()));
            System.out.println(oAuth2AccessToken.getExpiration());
            //System.out.println(map);
        } else {
            map.put("result", "ok");
            map.put("expireIn", String.valueOf(oAuth2AccessToken.getExpiresIn()));
            map.put("username", oAuth2AccessToken.getAdditionalInformation().get("username").toString());
            map.put("accesstoken", oAuth2AccessToken.getValue());
            map.put("active", getActiveDuration(new Timestamp(oAuth2AccessToken.getExpiration().getTime()).toLocalDateTime()));
        }
        return map;
    }

    @Override
    public Map<String, String> getTokenInfoByUsername(String clientId, String username) {
        Collection<OAuth2AccessToken> oAuth2AccessTokens = tokenStore.findTokensByClientIdAndUserName(clientId, username);
        //checking if there is a token with that username
        if (oAuth2AccessTokens.size() > 0) {
            OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokens.iterator().next();
            return getTokenInfoByToken(oAuth2AccessToken);
        } else
            return getTokenInfoByToken((String) null);//(String) to make it use getTokenInfoByToken(String)

    }

    @Override
    public Map<String, String> revokeToken() {

        Map<String, String> map = new HashMap<>(1);
        try {
            //get the current authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            OAuth2Authentication oAuth2 = ((OAuth2Authentication) authentication);

            removeAccessRefreshToken(tokenStore.getAccessToken(oAuth2));

            map.put("result", "revoked");
        } catch (Exception e) {
            map.put("result", "error");
        }
        return map;
    }

    @Override
    public Map<String, String> revokeToken(String clientId, String username) {
        Map<String, String> map = getTokenInfoByUsername(clientId, username);

        if (map.get("result").equals("ok") || map.get("result").equals("expired")) {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(map.get("accesstoken"));
            map.clear();
            if (removeAccessRefreshToken(accessToken))
                map.put("result", "revoked");
            else
                map.put("result", "error");
        }
        System.out.println(map);
        return map;
    }

    @Override
    public Map<String, String> revokeToken(String token) {
        return null;
    }

    @Override
    public boolean removeAccessRefreshToken(OAuth2AccessToken accessToken) {
        try {
            //remove access token
            tokenStore.removeAccessToken(accessToken);
            //remove refresh token
            OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
            tokenStore.removeRefreshToken(refreshToken);
            return true;
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    private String getActiveDuration(LocalDateTime fromDateTime) {
        LocalDateTime now = LocalDateTime.now();

        if (fromDateTime.isAfter(now))
            return "true";
        else {
            LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

            long years = tempDateTime.until(now, ChronoUnit.YEARS);
            tempDateTime = tempDateTime.plusYears(years);

            long months = tempDateTime.until(now, ChronoUnit.MONTHS);
            tempDateTime = tempDateTime.plusMonths(months);

            long days = tempDateTime.until(now, ChronoUnit.DAYS);
            tempDateTime = tempDateTime.plusDays(days);

            long hours = tempDateTime.until(now, ChronoUnit.HOURS);
            tempDateTime = tempDateTime.plusHours(hours);

            long minutes = tempDateTime.until(now, ChronoUnit.MINUTES);
            tempDateTime = tempDateTime.plusMinutes(minutes);

            long seconds = tempDateTime.until(now, ChronoUnit.SECONDS);

            System.out.println(years + "yr " +
                    months + "mo " +
                    days + "d " +
                    hours + "h " +
                    minutes + "m " +
                    seconds + "s.");

            String duration = "";
            if (minutes <= 0 && hours <= 0 && days <= 0 && months <= 0 && years <= 0)
                duration = seconds + "s ";
            if (days <= 0 && months <= 0 && years <= 0 && minutes > 0)
                duration = minutes + "m " + duration;
            if (months <= 0 && years <= 0 && hours > 0)
                duration = hours + "h " + duration;
            if (years <= 0 && days > 0)
                duration = days + "d " + duration;
            if (months > 0)
                duration = months + "mo " + duration;
            if (years > 0)
                duration = years + "yr " + duration;

            System.out.println(duration);

            return duration.trim();
        }


    }

}
