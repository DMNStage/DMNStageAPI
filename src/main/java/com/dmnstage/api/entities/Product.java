package com.dmnstage.api.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    // With Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // With SubProduct
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<SubProduct> SubProducts = new ArrayList<SubProduct>();

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
}
