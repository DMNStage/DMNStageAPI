package com.dmnstage.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SubProduct implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String pathName;

    //with Client
    @ManyToMany(mappedBy = "subProducts", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Client> clients = new ArrayList<>();

    // With Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public SubProduct() {
    }

    public SubProduct(String name, String pathName) {
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

    public void addClient(Client client) {
        clients.add(client);
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "SubProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pathName='" + pathName + '\'' +
                ", product=" + product +
                '}';
    }
}
