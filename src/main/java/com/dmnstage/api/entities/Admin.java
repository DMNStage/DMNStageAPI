package com.dmnstage.api.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User implements Serializable {

    private String firstName;

    private String lastName;

    public Admin() {
    }

    public Admin(String username, String password, String email, String phone, String firstName, String lastName) {
        super(username, password, email, phone);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Admin{" +
                super.toString() +
                " firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
