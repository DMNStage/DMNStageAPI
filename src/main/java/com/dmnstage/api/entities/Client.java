package com.dmnstage.api.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User implements Serializable {

    private String organizationName;

    public Client() {
    }

    public Client(String username, String password, String email, String phone, String organizationName) {
        super(username, password, email, phone);
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
