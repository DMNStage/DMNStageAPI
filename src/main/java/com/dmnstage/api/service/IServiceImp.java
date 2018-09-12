package com.dmnstage.api.service;

import com.dmnstage.api.entities.*;
import com.dmnstage.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
public class IServiceImp implements IService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SubProductRepository subProductRepository;
    private final ConfigRepository configRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public IServiceImp(UserRepository userRepository, ProductRepository productRepository,
                       SubProductRepository subProductRepository, ConfigRepository configRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.subProductRepository = subProductRepository;
        this.configRepository = configRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Config
    @Override
    public Config getConfigByKey(String key) {
        return configRepository.findBykey(key);
    }

    @Override
    public Config setConfig(Config config) {
        return configRepository.save(config);
    }

    //Relation between Classes
    @Override
    public void addSubProductToProduct(SubProduct subProduct, Product product) {
        product.addSubProduct(subProduct);
    }

//    @Override
//    public void addUserToRole(User user, Role role) { // Many To Many (Bi)
//        role.addUser(user);
//        user.addRole(role);
//    }

    @Override
    public void addUserToRole(User user, Role role) {
        role.addUser(user);
        user.setRole(role);
    }

//    @Override
//    public void addProductToCategory(Product Product, Category category) {
//        category.addProduct(Product);
//    }
//
//    @Override
//    public void mergeClientCategory(Client client, Category category) {
//        category.addClient(client);
//        client.addCategory(category);
//    }

    @Override
    public void mergeClientSubProduct(Client client, SubProduct subProduct) {
        subProduct.addClient(client);
        client.addSubProduct(subProduct);
    }

    //User
    @Override
    public User newUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User saveUser(User user) {

        return user;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllAdmins() {
        return userRepository.findAllAdmins();
    }

    @Override
    public List<User> getAllClients() {
        return userRepository.findAllClients();
    }

    @Override
    public List<String> getClientsBySubProduct(int id) {
        return userRepository.getClientsBySubProduct(id);
    }

    @Override
    public Admin setAdmin(Admin admin) {
        return userRepository.save(admin);
    }

    @Override
    public Client setClient(Client client) {
        return userRepository.save(client);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.delete(id);
    }

    //Role
    @Override
    public Role newRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.getByName(name);
    }

    //Product
    @Override
    public Product newProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product setProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.delete(id);
    }

    //SubProduct
    @Override
    public SubProduct newSubProduct(SubProduct subProduct) {
        return subProductRepository.save(subProduct);
    }

    @Override
    public SubProduct getSubProductById(int id) {
        return subProductRepository.findById(id);
    }

    @Override
    public List<SubProduct> getAllSubProducts() {
        return subProductRepository.findAll();
    }

    @Override
    public SubProduct setSubProduct(SubProduct subProduct) {
        return subProductRepository.save(subProduct);
    }

    @Override
    public void deleteSubProduct(int id) {
        subProductRepository.delete(id);
    }

}
