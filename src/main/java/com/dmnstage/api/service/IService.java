package com.dmnstage.api.service;

import com.dmnstage.api.entities.*;

import java.util.List;

public interface IService {
    //Relation between Classes
    void addSubProductToProduct(SubProduct subProduct, Product product);

    /* void addProductToCategory(Product Product, Category category);

     void mergeClientCategory(Client client, Category category);*/
    void mergeClientProduct(Client client, Product product);

    //User
    User newUser(User user);

    User saveUser(User user);

    User getUserById(int id);

    List<Admin> getAllAdmins();

    List<Client> getAllClients();


    //Product
    Product newProduct(Product product);

    Product getProductById(int id);

    List<Product> getAllProducts();

    //SubProduct
    SubProduct newSubProduct(SubProduct subProduct);

    SubProduct getSubProductById(int id);

    List<SubProduct> getAllSubProducts();
}
