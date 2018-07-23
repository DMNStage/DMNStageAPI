package com.dmnstage.api.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User implements Serializable {

    private String organizationName;
    // with Category
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "client_product",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

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

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Client{" +
                super.toString() +
                " organizationName='" + organizationName + '\'' +
                ", products=" + products +
                '}';
    }
}
