package com.dmnstage.api.web;

import com.dmnstage.api.entities.*;
import com.dmnstage.api.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestService {

    private final IService service;

    @Autowired
    public RestService(IService service) {
        this.service = service;
    }

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

    @RequestMapping(value = "/newadmin", method = RequestMethod.POST)
    public User newAdmin(@RequestBody Admin admin) {
        return service.newUser(admin);
    }

    @RequestMapping(value = "/newclient", method = RequestMethod.POST)
    public User newClient(@RequestBody Client client) {

        return service.newUser(client);
    }

    @RequestMapping(value = "/newpermission/{id}", method = RequestMethod.POST)
    public void setPermissionsToClient(String selectedSubProduct[], @PathVariable Integer id) {
        for (int i = 0; i < selectedSubProduct.length; i++) {
            SubProduct subProduct = service.getSubProductById(Integer.parseInt(selectedSubProduct[i]));
            //Permission p=new permission();
            // service.addPermissionToSubProduct(p,subProduct);
            // User u = service.getuserById(id);
            // service.addPermissionToClient(p,u);
        }


    }

    @RequestMapping(value = "/newproduct", method = RequestMethod.POST)
    public Product newProduct(@RequestBody Product product) {
        return service.newProduct(product);
    }

    @RequestMapping(value = "/newsubproduct/{id}", method = RequestMethod.POST)
    public SubProduct newSubProduct(@RequestBody SubProduct subProduct, @PathVariable Integer id) {
        Product product = service.getProductById(id);
        service.addSubProductToProduct(subProduct, product);
        return service.newSubProduct(subProduct);
    }


}
