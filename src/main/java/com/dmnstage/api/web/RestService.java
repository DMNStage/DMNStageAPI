package com.dmnstage.api.web;

import com.dmnstage.api.entities.*;
import com.dmnstage.api.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestService {

    @Autowired //injection des dependances
    private IService service;

    @RequestMapping(value = "/getuser/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Integer id) {
        return service.getUserById(id);
    }

    @RequestMapping(value = "/getadmins", method = RequestMethod.GET)
    public List<Admin> getAdmins() {
        return service.getAllAdmins();
    }

    @RequestMapping(value = "/getclients", method = RequestMethod.GET)
    public List<Client> getClients() {
        return service.getAllClients();
    }

    @RequestMapping(value = "/getcategory/{id}", method = RequestMethod.GET)
    public Category getCategory(@PathVariable Integer id) {
        return service.getCategoryById(id);
    }

    @RequestMapping(value = "/getcategories", method = RequestMethod.GET)
    public List<Category> getCategories() {
        return service.getAllCategories();
    }

    @RequestMapping(value = "/getproduct/{id}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable Integer id) {
        return service.getProductById(id);
    }

    @RequestMapping(value = "/getproducts", method = RequestMethod.GET)
    public List<Product> getProducts() {
        return service.getAllProducts();
    }

    @RequestMapping(value = "/getsubproduct/{id}", method = RequestMethod.GET)
    public SubProduct getSubProduct(@PathVariable Integer id) {
        return service.getSubProductById(id);
    }

    @RequestMapping(value = "/getsubproducts", method = RequestMethod.GET)
    public List<SubProduct> getSubProducts() {
        return service.getAllSubProducts();
    }
}
