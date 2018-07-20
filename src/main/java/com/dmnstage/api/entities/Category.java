package com.dmnstage.api.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String pathFormat;

    public Category(String name, String pathFormat) {
        this.name = name;
        this.pathFormat = pathFormat;
    }

    public Category() {
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

    public String getPathFormat() {
        return pathFormat;
    }

    public void setPathFormat(String pathFormat) {
        this.pathFormat = pathFormat;
    }
}
