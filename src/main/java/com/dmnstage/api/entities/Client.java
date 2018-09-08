package com.dmnstage.api.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User implements Serializable {

    private String organizationName;

    // with SubProduct
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "client_subProduct",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "subProduct_id")
    )
    private List<SubProduct> subProducts = new ArrayList<>();

    public Client() {
    }

    public Client(String username, String password, String email, String phone, boolean active, String organizationName) {
        super(username, password, email, phone, active);
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void addSubProduct(SubProduct subProduct) {
        subProducts.add(subProduct);
    }

    public List<SubProduct> getSubProducts() {
        return subProducts;
    }

    public void setSubProducts(List<SubProduct> subProducts) {
        this.subProducts = subProducts;
    }

    @Override
    public String toString() {
        return "Client{" +
                super.toString() +
                " organizationName='" + organizationName + '\'' +
                ", subProducts=" + subProducts +
                '}';
    }
}
