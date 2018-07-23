package com.dmnstage.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Config implements Serializable {
    @Id
    @Column(name = "ConfKey")
    private String key;
    @Column(name = "ConfVal")
    private String value;

    public Config(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Config() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
