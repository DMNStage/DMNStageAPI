package com.dmnstage.api.web;

import com.dmnstage.api.entities.*;
import com.dmnstage.api.service.IService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;

//import javax.json.JsonObject;
//import javax.json.JsonArray;


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

    //
    //CREATE
    //

    @RequestMapping(value = "/newadmin", method = RequestMethod.POST)
    public ResponseEntity<?> newAdmin(@RequestBody Admin admin) {

        if (service.getUserByUsername(admin.getUsername()) == null) {
            return new ResponseEntity<>(service.newUser(admin), HttpStatus.OK);
        }
        return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
    }
    /**
     * {
     * "client": {
     * "username": "b",
     * "password": "d",
     * "email": "d",
     * "phone": "d",
     * "organizationName": "f"
     * },
     * "selectedSubProduct": [
     * 1,
     * 2,
     * 3
     * ]
     * }
     */
    @RequestMapping(value = "/newclient", method = RequestMethod.POST)
    public ResponseEntity<?> newClient(@RequestBody String jsonStr) {

        JSONObject jObject = new JSONObject(jsonStr);
        JSONObject clientJson = jObject.getJSONObject("client");
        if (service.getUserByUsername(clientJson.getString("username")) == null) {
            Client client = new Client();
            client.setUsername(clientJson.getString("username"));
            client.setPassword(clientJson.getString("password"));
            client.setEmail(clientJson.getString("email"));
            client.setPhone(clientJson.getString("phone"));
            client.setOrganizationName(clientJson.getString("organizationName"));

            JSONArray subProductJsonArray = jObject.getJSONArray("selectedSubProduct");

            SubProduct subProduct;
            for (int i = 0; i < subProductJsonArray.length(); i++) {
                subProduct = service.getSubProductById(subProductJsonArray.getInt(i));
                service.mergeClientSubProduct(client, subProduct);
            }

            return new ResponseEntity<>(service.newUser(client), HttpStatus.OK);
        }

        return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/newproduct", method = RequestMethod.POST)
    public Product newProduct(@RequestBody Product product) {
        return service.newProduct(product);
    }

    @RequestMapping(value = "/newsubproduct/{id}", method = RequestMethod.POST)
    public SubProduct newSubProduct(@RequestBody SubProduct subProduct, @PathVariable(name = "id") Integer selectedProduct) {
        Product product = service.getProductById(selectedProduct);
        service.addSubProductToProduct(subProduct, product);
        return service.newSubProduct(subProduct);
    }

    //
    //UPDATE
    //

    @RequestMapping(value = "/setconfig", method = RequestMethod.PUT)
    public ResponseEntity<?> setConfig(@RequestBody Config config) {
        config.setKey("pathFormat");
        return new ResponseEntity<>(service.setConfig(config), HttpStatus.OK);
    }

    @RequestMapping(value = "/setadmin/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> setAdmin(@RequestBody Admin admin, @PathVariable Integer id) {

        User userInDB = service.getUserById(id);
        if (userInDB.getUsername().equals(admin.getUsername()) ||
                (!userInDB.getUsername().equals(admin.getUsername()) && service.getUserByUsername(admin.getUsername()) == null)) {
            if (admin.getPassword() == null || (admin.getPassword() != null && admin.getPassword().isEmpty()))
                admin.setPassword(userInDB.getPassword());
            else {
                //hash admin.setPassword( HASH(admin.getPassword))
                admin.setPassword("HASH:" + admin.getPassword());
            }
            admin.setId(id);
            return new ResponseEntity<>(service.setAdmin(admin), HttpStatus.OK);
        }
        return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/setclient/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> setClient(@RequestBody String jsonStr, @PathVariable Integer id) {

        JSONObject jObject = new JSONObject(jsonStr);
        JSONObject clientJson = jObject.getJSONObject("client");
        Client clientForm = new Client();
        clientForm.setUsername(clientJson.getString("username"));
        clientForm.setPassword(clientJson.getString("password"));
        clientForm.setEmail(clientJson.getString("email"));
        clientForm.setPhone(clientJson.getString("phone"));
        clientForm.setOrganizationName(clientJson.getString("organizationName"));

        //Getting user from DB
        User clientInDB = service.getUserById(id);
        // Checking if the username hasn't been changed or it already exist
        if (clientInDB.getUsername().equals(clientForm.getUsername()) ||
                (!clientInDB.getUsername().equals(clientForm.getUsername()) &&
                        service.getUserByUsername(clientForm.getUsername()) == null)) {
            //Checking if the password from the Form is empty
            if (clientForm.getPassword() == null || (clientForm.getPassword() != null && clientForm.getPassword().isEmpty()))
                clientForm.setPassword(clientInDB.getPassword());//if it's empty, get the password from the DB
            else {
                //hash clientForm.setPassword( HASH(clientForm.getPassword))
                clientForm.setPassword("HASH:" + clientForm.getPassword());//If not hash the new password
            }
            clientForm.setId(id);

            JSONArray subProductJsonArray = jObject.getJSONArray("selectedSubProduct");

            SubProduct subProduct;
            for (int i = 0; i < subProductJsonArray.length(); i++) {
                subProduct = service.getSubProductById(subProductJsonArray.getInt(i));
                service.mergeClientSubProduct(clientForm, subProduct);
            }
            return new ResponseEntity<>(service.setClient(clientForm), HttpStatus.OK);
        }
        return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/setproduct/{id}", method = RequestMethod.PUT)
    public Product newProduct(@RequestBody Product product, @PathVariable Integer id) {
        product.setId(id);
        return service.setProduct(product);
    }

    @RequestMapping(value = "/setsubproduct/{subProductId}/{productId}", method = RequestMethod.PUT)
    public SubProduct setSubProduct(@RequestBody SubProduct subProduct, @PathVariable Integer subProductId, @PathVariable Integer productId) {
        subProduct.setId(subProductId);
        Product product = service.getProductById(productId);
        service.addSubProductToProduct(subProduct, product);
        return service.setSubProduct(subProduct);
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

    //
    // service.setConfig(new Config("pathFormat", "http://img.dmnstage.com/teledetection/#product#/#subProduct#/#year#-#month#-#day#/#hour##minute#.#ext#"));
    //
    @RequestMapping(value = "/getimagestime/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getImagesTime(@PathVariable Integer id) {
        SubProduct subProduct = service.getSubProductById(id);
        JSONObject obj = new JSONObject();
        obj.put("startTime", subProduct.getStartTime());
        obj.put("endTime", subProduct.getEndTime());
        obj.put("step", subProduct.getStep());
        System.out.println(obj.toString());
        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getimagestime2/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getImagesTime2(@PathVariable Integer id) {
        SubProduct subProduct = service.getSubProductById(id);
        JSONObject jsonObject = new JSONObject();
        LocalTime iteratorTime = subProduct.getStartTime();
        do {
            jsonObject.accumulate("imageTime", iteratorTime);
            iteratorTime = iteratorTime.plusMinutes(subProduct.getStep());
        }
        while (!iteratorTime.equals(subProduct.getEndTime().plusMinutes(subProduct.getStep())));

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getimage/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getImages(@PathVariable Integer id,
                                       @RequestParam String year,
                                       @RequestParam String month,
                                       @RequestParam String day,
                                       @RequestParam(required = false) String hour,
                                       @RequestParam(required = false) String minute
    ) {
        SubProduct subProduct = service.getSubProductById(id);
        String url = service.getConfigByKey("pathFormat").getValue();

        url = url.replace("#product#", subProduct.getProduct().getPathName());
        url = url.replace("#subProduct#", subProduct.getPathName());
        url = url.replace("#year#", year);
        url = url.replace("#month#", month);
        url = url.replace("#day#", day);
        url = url.replace("#ext#", subProduct.getExt());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subProductId", subProduct.getId());
        jsonObject.put("subProductName", subProduct.getName());

        if (hour != null && minute != null) {

            url = url.replace("#hour#", hour);
            url = url.replace("#minute#", minute);

            jsonObject.put("img", url);

            HttpURLConnection.setFollowRedirects(false);
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setRequestMethod("HEAD");

                jsonObject.put("httpStatus", httpURLConnection.getResponseCode());
            } catch (IOException e) {
                //e.printStackTrace();
                return new ResponseEntity<>("IOException", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } else {

            LocalTime iteratorTime = subProduct.getStartTime();
            String tempHour, tempMinute;

            do {
                if (iteratorTime.getHour() < 10)
                    tempHour = "0" + String.valueOf(iteratorTime.getHour());
                else
                    tempHour = String.valueOf(iteratorTime.getHour());

                if (iteratorTime.getMinute() < 10)
                    tempMinute = "0" + String.valueOf(iteratorTime.getMinute());
                else
                    tempMinute = String.valueOf(iteratorTime.getMinute());

                url = url.replace("#hour##minute#", tempHour + tempMinute);
                jsonObject.accumulate("img", url);
                url = url.replace(tempHour + tempMinute, "#hour##minute#");

                iteratorTime = iteratorTime.plusMinutes(subProduct.getStep());
            }
            while (!iteratorTime.equals(subProduct.getEndTime().plusMinutes(subProduct.getStep())));

            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
    }
}
