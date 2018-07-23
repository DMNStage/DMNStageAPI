package com.dmnstage.api.service;

import com.dmnstage.api.entities.*;
import com.dmnstage.api.repositories.CategoryRepository;
import com.dmnstage.api.repositories.ProductRepository;
import com.dmnstage.api.repositories.SubProductRepository;
import com.dmnstage.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@Transactional
public class IServiceImp implements IService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SubProductRepository subProductRepository;

    @Override
    public void addSubProductToProduct(SubProduct subProduct, Product product) {
        product.addSubProduct(subProduct);
    }

    @Override
    public void addProductToCategory(Product Product, Category category) {
        category.addProduct(Product);
    }

    @Override
    public void mergeClientCategory(Client client, Category category) {
        category.addClient(client);
        client.addCategory(category);
    }

    @Override
    public User newUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsername().toLowerCase());
        //user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
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
    public List<Admin> getAllAdmins() {
        return userRepository.findAllAdmins();
    }

    @Override
    public List<Client> getAllClients() {
        return userRepository.findAllClients();
    }

    @Override
    public Category newCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

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


}
