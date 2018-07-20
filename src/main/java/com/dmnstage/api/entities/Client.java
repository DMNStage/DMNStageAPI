package com.dmnstage.api.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User implements Serializable {

    private String organizationName;

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

    // with Category
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "client_category",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<Category>();

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    //-----
}
