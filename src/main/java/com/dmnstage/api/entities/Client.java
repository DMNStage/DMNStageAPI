package com.dmnstage.api.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User implements Serializable {

    private int organizationName;
}
