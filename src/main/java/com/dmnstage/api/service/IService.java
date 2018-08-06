package com.dmnstage.api.service;

import com.dmnstage.api.entities.*;

import java.util.List;

public interface IService {
    //Config
    Config getConfigByKey(String key);

    Config setConfig(Config config);

    //Relation between Classes
    void addSubProductToProduct(SubProduct subProduct, Product product);

    void mergeClientSubProduct(Client client, SubProduct subProduct);

    void mergeUsersRole(User user, Role role);

    //User
    User newUser(User user);

    User saveUser(User user);

    User getUserById(int id);

    User getUserByUsername(String username);

    List<Admin> getAllAdmins();

    List<Client> getAllClients();

    Admin setAdmin(Admin admin);

    Client setClient(Client client);

    void deleteUser(int id);

    //Product
    Product newProduct(Product product);

    Product getProductById(int id);

    List<Product> getAllProducts();

    Product setProduct(Product product);

    void deleteProduct(int id);

    //Role
    Role newRole(Role role);

    //SubProduct
    SubProduct newSubProduct(SubProduct subProduct);

    SubProduct getSubProductById(int id);

    List<SubProduct> getAllSubProducts();

    SubProduct setSubProduct(SubProduct subProduct);

    void deleteSubProduct(int id);
}
