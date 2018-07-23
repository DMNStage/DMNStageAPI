package com.dmnstage.api.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Configaasqs implements Serializable {
    @Id
    private String key;
    private String value;

    public Configaasqs(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Configaasqs() {
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
