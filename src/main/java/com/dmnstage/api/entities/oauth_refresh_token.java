package com.dmnstage.api.entities;

import javax.persistence.*;

@Entity
public class oauth_refresh_token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token_id;
    @Column(length = 1000)
    private byte[] token;
    @Column(length = 1000)
    private byte[] authentication;

}
