package com.dmnstage.api.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class SubProduct implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String nameInPath;

    public SubProduct(String name, String nameInPath) {
        this.name = name;
        this.nameInPath = nameInPath;
    }

    public SubProduct() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameInPath() {
        return nameInPath;
    }

    public void setNameInPath(String nameInPath) {
        this.nameInPath = nameInPath;
    }
}
