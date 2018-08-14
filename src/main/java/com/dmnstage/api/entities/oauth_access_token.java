package com.dmnstage.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class oauth_access_token {
    private String token_id;

    @Column(length = 1000)
    private byte[] token;

    @Id
    private String authentication_id;

    private String user_name;

    private String client_id;

    @Column(length = 1000)
    private byte[] authentication;

    private String refresh_token;

}
