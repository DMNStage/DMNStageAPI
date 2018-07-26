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

    //
    //SELLECT
    //
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

    //
    //CREATE
    //
    @RequestMapping(value = "/newclient", method = RequestMethod.POST)
    public User newClient(@RequestBody Client client /*, String selectedSubProduct[] */) {

        String selectedSubProduct[] = new String[3];
        selectedSubProduct[0] = "1";
        selectedSubProduct[1] = "2";
        selectedSubProduct[2] = "3";

        User newClient = service.newUser(client);

        SubProduct subProduct;

        for (String aSelectedSubProduct : selectedSubProduct) {
            subProduct = service.getSubProductById(Integer.parseInt(aSelectedSubProduct));
            service.mergeClientSubProduct((Client) newClient, subProduct);
        }
        return newClient;
    }

    @RequestMapping(value = "/newproduct", method = RequestMethod.POST)
    public Product newProduct(@RequestBody Product product) {
        return service.newProduct(product);
    }

    /*
    @RequestMapping(value = "/newsubproduct/{id}", method = RequestMethod.POST)
    public SubProduct newSubProduct(@RequestBody SubProduct subProduct, @PathVariable Integer id) {
        Product product = service.getProductById(id);
        service.addSubProductToProduct(subProduct, product);
        return service.newSubProduct(subProduct);
    }
    */

    @RequestMapping(value = "/newsubproduct", method = RequestMethod.POST)
    public SubProduct newSubProduct(@RequestBody SubProduct subProduct/*, String selectedProduct */) {
        String selectedProduct = "1";
        Product product = service.getProductById(Integer.parseInt(selectedProduct));
        service.addSubProductToProduct(subProduct, product);
        return service.newSubProduct(subProduct);
    }

    //
    //UPDATE
    //
    @RequestMapping(value = "/setadmin/{id}", method = RequestMethod.PUT)
    public User setAdmin(@RequestBody Admin admin, @PathVariable Integer id) {
        admin.setId(id);
        return service.setAdmin(admin);
    }

    @RequestMapping(value = "/setclient/{id}", method = RequestMethod.PUT)
    public User setClient(@RequestBody Client client, @PathVariable Integer id /*, String selectedSubProduct[] */) {

        String selectedSubProduct[] = new String[3];
        selectedSubProduct[0] = "1";
        selectedSubProduct[1] = "2";
        selectedSubProduct[2] = "3";

        client.setId(id);
        User modifiedClient = service.setClient(client);

        SubProduct subProduct;

        for (String aSelectedSubProduct : selectedSubProduct) {
            subProduct = service.getSubProductById(Integer.parseInt(aSelectedSubProduct));
            service.mergeClientSubProduct((Client) modifiedClient, subProduct);
        }

        return modifiedClient;
    }

    @RequestMapping(value = "/setproduct/{id}", method = RequestMethod.PUT)
    public Product newProduct(@RequestBody Product product, @PathVariable Integer id) {
        product.setId(id);
        return service.setProduct(product);
    }

    @RequestMapping(value = "/setsubproduct/{id}", method = RequestMethod.PUT)
    public SubProduct newSubProduct(@RequestBody SubProduct subProduct, @PathVariable Integer id /*, String selectedProduct */) {
        String selectedProduct = "2"; // ghi exemple
        subProduct.setId(id);
        SubProduct modifiedSubProduct = service.setSubProduct(subProduct);
        Product product = service.getProductById(Integer.parseInt(selectedProduct));
        service.addSubProductToProduct(modifiedSubProduct, product);
        return modifiedSubProduct;
    }

    //
    //DELETE
    //
    @RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Integer id) {
        service.deleteUser(id);
    }

    @RequestMapping(value = "/deleteproduct/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable Integer id) {
        service.deleteProduct(id);
    }

    @RequestMapping(value = "/deletesubproduct/{id}", method = RequestMethod.DELETE)
    public void deleteSubProduct(@PathVariable Integer id) {
        service.deleteSubProduct(id);
    }

}
