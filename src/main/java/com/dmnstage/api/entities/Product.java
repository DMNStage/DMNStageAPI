package com.dmnstage.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String pathName;

    // With SubProduct
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<SubProduct> SubProducts = new ArrayList<>();

    public Product() {
    }

    public Product(String name, String pathName) {
        this.name = name;
        this.pathName = pathName;
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

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public void addSubProduct(SubProduct subProduct) {
        SubProducts.add(subProduct);
        subProduct.setProduct(this);
    }

    public List<SubProduct> getSubProducts() {
        return SubProducts;
    }

    public void setSubProducts(List<SubProduct> subProducts) {
        SubProducts = subProducts;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pathName='" + pathName + '\'' +
                ", SubProducts=" + SubProducts +
                '}';
    }
}
