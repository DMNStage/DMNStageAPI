package com.dmnstage.api.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
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

    // With Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
