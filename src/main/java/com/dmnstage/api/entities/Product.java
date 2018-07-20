package com.dmnstage.api.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String nameInPath;

    public Product(String name, String nameInPath) {
        this.name = name;
        this.nameInPath = nameInPath;
    }

    public Product() {
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
