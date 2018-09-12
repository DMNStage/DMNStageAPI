package com.dmnstage.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class oauth_access_token implements Serializable {
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
