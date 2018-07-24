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
    // NaitSaid version of newclient : bla mandiro table perm ghi many to many mabin client et subproduct kafiya (chof class diag)
    // mohim ana ghadi ndirhom bjouj ghi bach ibano lik que c la meme chose (many to many mabin client et subproduct ou bien nzido  class permission)
    // had la version rah incha2allah ghadi tkhdem ghi b methode wa7da no need ndiro 2 :D
    @RequestMapping(value = "/newclient", method = RequestMethod.POST)
    public User newClient(@RequestBody Client client /*, String selectedSubProduct[] mli ghadi ndiro l'interface html ghadi nkhedmo b selectedSubProduct[] */) {

        String selectedSubProduct[] = new String[3]; //ghi exemple
        selectedSubProduct[0] = "1";
        selectedSubProduct[1] = "2";
        selectedSubProduct[2] = "3";

        User newClient = service.newUser(client);

        SubProduct subProduct;

        for (int i = 0; i < selectedSubProduct.length; i++) {
            subProduct = service.getSubProductById(Integer.parseInt(selectedSubProduct[i]));

            // ila derna many to many mabin client et subproduc
            service.mergeClientSubProduct((Client) newClient, subProduct);
            /*
            le resultat dial hadi ghadi ikoun le meme dial ila zedna class permission, le resultat ghadi ikoun par exemple:
            ---------------------------
            |client_id |sub_product_id|
            ---------------------------
            |        5 | 2            |
            |        5 | 1            |
            |        5 | 3            |
            ---------------------------
            client 5 3endo perm bach ichof subprod 2,1 et 3
            */

            /* ila zedna Class Permission
            Permission permission = new permission();
            service.addPermissionToSubProduct(permission,subProduct);
            service.addPermissionToClient(permission,newclient);
            */
        }
        return newClient;
    }

    /*
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
    */

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
        String selectedProduct = "1"; // ghi exemple
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

        for (int i = 0; i < selectedSubProduct.length; i++) {
            subProduct = service.getSubProductById(Integer.parseInt(selectedSubProduct[i]));

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
