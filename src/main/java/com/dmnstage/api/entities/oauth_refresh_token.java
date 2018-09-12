package com.dmnstage.api.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class oauth_refresh_token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token_id;
    @Column(length = 1000)
    private byte[] token;
    @Column(length = 1000)
    private byte[] authentication;

}
