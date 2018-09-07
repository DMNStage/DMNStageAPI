package com.dmnstage.api.web;

import com.dmnstage.api.entities.*;
import com.dmnstage.api.service.IService;
import com.dmnstage.api.service.ITokenService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//import javax.json.JsonObject;
//import javax.json.JsonArray;

@CrossOrigin("*")
@RestController
public class RestService {

    private final IService service;

    private final PasswordEncoder passwordEncoder;

    private final ITokenService tokenService;

    @Autowired
    public RestService(IService service, PasswordEncoder passwordEncoder, ITokenService tokenService) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @RequestMapping("/")
    @ResponseBody
    public void welcome(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://dmnstage.com");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tokens")
    public ResponseEntity<?> getTokens() {
        return new ResponseEntity<>(tokenService.getAllTokensInfoByClientId("ClientId"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tokeninfo", produces = "application/json; charset=utf-8")
    public ResponseEntity<?> getTokenInfo(@RequestParam String by,
                                          @RequestParam(required = false) String token,
                                          @RequestParam(required = false, name = "clientid") String clientId,
                                          @RequestParam(required = false) String username
    ) {

        if (by.equals("token") && !(token == null || token.trim().equals(""))) {
            Map<String, String> map = tokenService.getTokenInfoByToken(token);

            HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
            switch (map.get("result")) {
                case "ok":
                    httpStatus = HttpStatus.OK;
                    break;
                case "notfound":
                    httpStatus = HttpStatus.NOT_FOUND;
                    break;
                case "expired":
                    httpStatus = HttpStatus.GONE;
                    break;
            }
            return new ResponseEntity<>(map, httpStatus); //Checking if the result is notfound or expired so we return 404 instead of 200
        } else if (by.equals("username") && !(username == null || username.trim().equals("")) && !(clientId == null || clientId.trim().equals(""))) {
            Map<String, String> map = tokenService.getTokenInfoByUsername(clientId, username);

            HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
            switch (map.get("result")) {
                case "ok":
                    httpStatus = HttpStatus.OK;
                    break;
                case "notfound":
                    httpStatus = HttpStatus.NOT_FOUND;
                    break;
                case "expired":
                    httpStatus = HttpStatus.GONE;
                    break;
            }
            return new ResponseEntity<>(map, httpStatus);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "Invalid Params");
        jsonObject.put("description", "The parameter 'by' should be either: =token with 'token' parameter, or =username with 'username' and 'clientid' parameters");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
    }

    //if it get the username param it disconnect the user with that username if not it disconnect the connected user that sent that request
    @RequestMapping(value = "/revoke_token", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity<?> revokeToken(@RequestParam(required = false, name = "clientid") String clientId,
                                         @RequestParam(required = false) String username) {
        Map<String, String> map;
        if ((clientId == null || clientId.trim().equals("")) && (username == null || username.trim().equals(""))) {
            map = tokenService.revokeToken();

        } else if (!(clientId == null || clientId.trim().equals("")) && !(username == null || username.trim().equals(""))) {
            map = tokenService.revokeToken("clientId", username);
        } else {
            map = new HashMap<>();
            map.put("result", "error");
            map.put("description", "Invalid parameter (Either pass no parameters or pass the 'username' and 'clientid' parameters)");
        }

        HttpStatus httpStatus;

        switch (map.get("result")) {
            case "revoked":
                httpStatus = HttpStatus.OK;
                break;
            case "notfound":
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            default:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
        }
        return new ResponseEntity<>(map, httpStatus);
    }


    @RequestMapping(value = "/revoke_all_tokens", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity<?> revokeAllTokensByClientId(@RequestParam(required = false,
            name = "clientid", defaultValue = "ClientId") String clientId) {
        Map<String, String> map;
        if (!(clientId == null || clientId.trim().equals(""))) {
            map = tokenService.revokeAllTokensByClientID(clientId);

        } else {
            map = new HashMap<>();
            map.put("result", "error");
            map.put("description", "Invalid parameter (Either pass no parameters or pass the 'username' and 'clientid' parameters)");
        }

        return new ResponseEntity<>(map, (map.get("result").equalsIgnoreCase("ok") ? HttpStatus.OK : HttpStatus.BAD_REQUEST));
    }

    //
    // Users
    //
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String type) {
        if(Objects.equals(type, "admin"))
            return new ResponseEntity<>(service.getAllAdmins(), HttpStatus.OK);
        else if(Objects.equals(type, "client"))
            return new ResponseEntity<>(service.getAllClients(), HttpStatus.OK);
        else
            return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/{id}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        User user;
        if((user = service.getUserById(id)) == null){
            return new ResponseEntity<>("{\"result\":\"L'utilisateur n'existe pas\"}", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/admin", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<?> newAdmin(@RequestBody Admin admin) {

        if (service.getUserByUsername(admin.getUsername()) == null) {
            service.addUserToRole(admin,service.getRoleByName("admin"));
            return new ResponseEntity<>(service.newUser(admin), HttpStatus.OK);
        }
        return new ResponseEntity<>(  "{\"result\":\"Ce nom d'utilisateur existe deja\"}", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/client", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<?> nclient(@RequestBody Client client) {

        if (service.getUserByUsername(client.getUsername()) == null) {

            SubProduct subProduct;
            for (int i = 0; i < client.getSubProducts().size(); i++) {
                subProduct = service.getSubProductById(client.getSubProducts().get(i).getId());
                client.getSubProducts().remove(i);
                //mergeClientSubProduct
                client.getSubProducts().add(i,subProduct);
                subProduct.addClient(client);
            }

            //another solution
//            List<SubProduct> subProducts = new ArrayList<>();
//            SubProduct subProduct;
//            for (int i = 0; i < client.getSubProducts().size(); i++) {
//                subProduct = service.getSubProductById(client.getSubProducts().get(i).getId());
//                subProducts.add(subProduct);
//            }
//            client.getSubProducts().clear();
//            for (int i = 0; i < subProducts.size(); i++) {
//                service.mergeClientSubProduct(client, subProducts.get(i));
//            }

            service.addUserToRole(client,service.getRoleByName("client"));
            return new ResponseEntity<>(service.newUser(client), HttpStatus.OK);
        }
        return new ResponseEntity<>(  "{\"result\":\"Ce nom d'utilisateur existe deja\"}", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/admin/{id}", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<?> newAdmin(@RequestBody Admin admin, @PathVariable Integer id) {
        User adminInDB = service.getUserById(id);
        if (adminInDB.getUsername().equals(admin.getUsername()) ||
                (!adminInDB.getUsername().equals(admin.getUsername()) && service.getUserByUsername(admin.getUsername()) == null)) {
            if (admin.getPassword() == null || (admin.getPassword() != null && admin.getPassword().isEmpty()))
                admin.setPassword(adminInDB.getPassword());
            else {
                //Hashing
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
            service.addUserToRole(admin,adminInDB.getRole());
            admin.setId(id);
            return new ResponseEntity<>(service.setAdmin(admin), HttpStatus.OK);
        }
        return new ResponseEntity<>(  "{\"result\":\"Ce nom d'utilisateur existe deja\"}", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/client/{id}", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<?> nclient(@RequestBody Client client, @PathVariable Integer id) {
        User clientInDB = service.getUserById(id);
        if (clientInDB.getUsername().equals(client.getUsername()) ||
                (!clientInDB.getUsername().equals(client.getUsername()) && service.getUserByUsername(client.getUsername()) == null)) {
            if (client.getPassword() == null || (client.getPassword() != null && client.getPassword().isEmpty()))
                client.setPassword(clientInDB.getPassword());
            else {
                //Hashing
                client.setPassword(passwordEncoder.encode(client.getPassword()));
            }
            SubProduct subProduct;
            for (int i = 0; i < client.getSubProducts().size(); i++) {
                subProduct = service.getSubProductById(client.getSubProducts().get(i).getId());
                client.getSubProducts().remove(i);
                //mergeClientSubProduct
                client.getSubProducts().add(i,subProduct);
                subProduct.addClient(client);
            }
            service.addUserToRole(client,clientInDB.getRole());
            client.setId(id);
            return new ResponseEntity<>(service.setClient(client), HttpStatus.OK);
        }
        return new ResponseEntity<>(  "{\"result\":\"Ce nom d'utilisateur existe deja\"}", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/{id}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        User user = service.getUserById(id);
        if(user == null){
            return new ResponseEntity<>("{\"result\":\"L'utilisateur n'existe pas\"}", HttpStatus.NOT_FOUND);
        }else{
            service.deleteUser(id);
            return new ResponseEntity<>(  "{\"result\":\"L'utilisateur a été supprimé\"}", HttpStatus.OK);
        }
    }

    //      {
//          "client": {
//              "username": "b",
//              "password": "d",
//              "email": "d",
//              "phone": "d",
//              "active": 1,
//              "organizationName": "f"
//          },
//          "selectedSubProduct": [1, 2, 3]
//      }

//        {
//            "username": "aaa",
//            "password": "aa",
//            "email": "aa@aa.com",
//            "phone": "+21200000000",
//            "active": 1,
//            "organizationName": "aa",
//            "subProducts": [
//                {"id": 2 },
//                {"id": 3 }
//            ]
//         }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/users", produces = "application/json", method = RequestMethod.POST)
//    public ResponseEntity<?> newClient(@RequestBody String jsonStr, @RequestParam String type) {
//
//        if(Objects.equals(type, "client")){
//            JSONObject jObject = new JSONObject(jsonStr);
//            JSONObject clientJson = jObject.getJSONObject("client");
//            if (service.getUserByUsername(clientJson.getString("username")) == null) {
//                Client client = new Client();
//                client.setUsername(clientJson.getString("username"));
//                client.setPassword(clientJson.getString("password"));
//                client.setEmail(clientJson.getString("email"));
//                client.setPhone(clientJson.getString("phone"));
//                client.setOrganizationName(clientJson.getString("organizationName"));
//                client.setActive(clientJson.getInt("active"));
//                service.addUserToRole(client,service.getRoleByName("client"));
//                JSONArray subProductJsonArray = jObject.getJSONArray("selectedSubProduct");
//
//                SubProduct subProduct;
//                for (int i = 0; i < subProductJsonArray.length(); i++) {
//                    subProduct = service.getSubProductById(subProductJsonArray.getInt(i));
//                    service.mergeClientSubProduct(client, subProduct);
//                }
//
//                return new ResponseEntity<>(service.newUser(client), HttpStatus.OK);
//            }
//
//            return new ResponseEntity<>(  "{\"result\":\"Ce nom d'utilisateur existe deja\"}", HttpStatus.BAD_REQUEST);
//        }
//        else if(Objects.equals(type, "admin")){
//            JSONObject adminJson = new JSONObject(jsonStr);
//            if (service.getUserByUsername(adminJson.getString("username")) == null) {
//                Admin admin = new Admin();
//
//                admin.setUsername(adminJson.getString("username"));
//                admin.setPassword(adminJson.getString("password"));
//                admin.setEmail(adminJson.getString("email"));
//                admin.setPhone(adminJson.getString("phone"));
//                admin.setFirstName(adminJson.getString("firstname"));
//                admin.setLastName(adminJson.getString("lastname"));
//                admin.setActive(adminJson.getInt("active"));
//                service.addUserToRole(admin,service.getRoleByName("admin"));
//                return new ResponseEntity<>(service.newUser(admin), HttpStatus.OK);
//            }
//            return new ResponseEntity<>(  "{\"result\":\"Ce nom d'utilisateur existe deja\"}", HttpStatus.BAD_REQUEST);
//        }
//        else
//            return new ResponseEntity<>(  "{\"result\":\"Operation échouée, le type d'utilisateur est manquant\"}", HttpStatus.BAD_REQUEST);
//    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<?> setClient(@RequestBody String jsonStr, @RequestParam String type, @PathVariable Integer id) {
//        if(Objects.equals(type, "client")){
//            JSONObject jObject = new JSONObject(jsonStr);
//            JSONObject clientJson = jObject.getJSONObject(type);
//
//            Client clientForm = new Client();
//            clientForm.setUsername(clientJson.getString("username"));
//            clientForm.setPassword(clientJson.getString("password"));
//            clientForm.setEmail(clientJson.getString("email"));
//            clientForm.setPhone(clientJson.getString("phone"));
//            clientForm.setOrganizationName(clientJson.getString("organizationName"));
//            clientForm.setActive(clientJson.getInt("active"));
//            service.addUserToRole(clientForm,service.getRoleByName("client"));
//
//            //Getting user from DB
//            User clientInDB = service.getUserById(id);
//            // Checking if the username hasn't been changed or it already exist
//            if (clientInDB.getUsername().equals(clientForm.getUsername()) ||
//                    (!clientInDB.getUsername().equals(clientForm.getUsername()) &&
//                            service.getUserByUsername(clientForm.getUsername()) == null)) {
//                //Checking if the password from the Form is empty
//                if (clientForm.getPassword() == null || (clientForm.getPassword() != null && clientForm.getPassword().isEmpty()))
//                    clientForm.setPassword(clientInDB.getPassword());//if it's empty, get the password from the DB
//                else {
//                    //Hashing
//                    clientForm.setPassword(passwordEncoder.encode(clientForm.getPassword()));//If not hash the new password
//                }
//                clientForm.setId(id);
//
//                JSONArray subProductJsonArray = jObject.getJSONArray("selectedSubProduct");
//
//                SubProduct subProduct;
//                for (int i = 0; i < subProductJsonArray.length(); i++) {
//                    subProduct = service.getSubProductById(subProductJsonArray.getInt(i));
//                    service.mergeClientSubProduct(clientForm, subProduct);
//                }
//                return new ResponseEntity<>(service.setClient(clientForm), HttpStatus.OK);
//            }
//            return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
//        }
//        else if (Objects.equals(type, "admin")){
//            JSONObject adminJson = new JSONObject(jsonStr);
//
//            Admin adminForm = new Admin();
//            adminForm.setUsername(adminJson.getString("username"));
//            adminForm.setPassword(adminJson.getString("password"));
//            adminForm.setEmail(adminJson.getString("email"));
//            adminForm.setPhone(adminJson.getString("phone"));
//            adminForm.setFirstName(adminJson.getString("firstname"));
//            adminForm.setLastName(adminJson.getString("lastname"));
//            adminForm.setActive(adminJson.getInt("active"));
//            service.addUserToRole(adminForm,service.getRoleByName("admin"));
//
//            //Getting user from DB
//            User clientInDB = service.getUserById(id);
//            // Checking if the username hasn't been changed or it already exist
//            if (clientInDB.getUsername().equals(adminForm.getUsername()) ||
//                    (!clientInDB.getUsername().equals(adminForm.getUsername()) &&
//                            service.getUserByUsername(adminForm.getUsername()) == null)) {
//                //Checking if the password from the Form is empty
//                if (adminForm.getPassword() == null || (adminForm.getPassword() != null && adminForm.getPassword().isEmpty()))
//                    adminForm.setPassword(clientInDB.getPassword());//if it's empty, get the password from the DB
//                else {
//                    //Hashing
//                    adminForm.setPassword(passwordEncoder.encode(adminForm.getPassword()));//If not hash the new password
//                }
//                adminForm.setId(id);
//                return new ResponseEntity<>(service.setAdmin(adminForm), HttpStatus.OK);
//            }
//            return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
//        }
//        else{
//            return new ResponseEntity<>("{\"result\":\"Operation échouée, le type d'utilisateur est manquant\"}", HttpStatus.BAD_REQUEST);
//        }
//
//    }

    //
    // products
    //
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable Integer id) {
        return service.getProductById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> getProducts() {
        return service.getAllProducts();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public Product newProduct(@RequestBody Product product) {
        return service.newProduct(product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    public Product newProduct(@RequestBody Product product, @PathVariable Integer id) {
        product.setId(id);
        return service.setProduct(product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/products/{id}", produces = "application/json",        method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        service.deleteProduct(id);
        return new ResponseEntity<>("{\"result\":\"Le produit a été supprimé\"}", HttpStatus.OK);
    }

    //
    // SubProducts
    //
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @RequestMapping(value = "/subproducts/{id}", method = RequestMethod.GET)
    public SubProduct getSubProduct(@PathVariable Integer id) {
        return service.getSubProductById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @RequestMapping(value = "/subproducts", method = RequestMethod.GET)
    public List<SubProduct> getSubProducts() {
        return service.getAllSubProducts();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/subproducts", method = RequestMethod.POST)
    public SubProduct newSubProduct(@RequestBody SubProduct subProduct, @RequestParam(name = "selectedproduct") String selectedProduct) {
        Product product = service.getProductById(Integer.parseInt(selectedProduct));
        service.addSubProductToProduct(subProduct, product);
        return service.newSubProduct(subProduct);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/subproducts/{subProductId}", method = RequestMethod.PUT)
    public SubProduct setSubProduct(@RequestBody SubProduct subProduct, @PathVariable Integer subProductId, @RequestParam(name = "productid") String productId) {
        subProduct.setId(subProductId);
        Product product = service.getProductById(Integer.parseInt(productId));
        service.addSubProductToProduct(subProduct, product);
        return service.setSubProduct(subProduct);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/subproducts/{id}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSubProduct(@PathVariable Integer id) {
        service.deleteSubProduct(id);
        return new ResponseEntity<>("{\"result\":\"Le sous-produit a été supprimé\"}", HttpStatus.OK);
    }

    //
    //Config
    //

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/config/{key}", method = RequestMethod.GET)
    public Config getConfig(@PathVariable String key) {
        return service.getConfigByKey(key);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/config", method = RequestMethod.PUT)
    public ResponseEntity<?> setConfig(@RequestBody Config config) {
        config.setKey("pathFormat");
        return new ResponseEntity<>(service.setConfig(config), HttpStatus.OK);
    }



    //
    // service.setConfig(new Config("pathFormat", "http://img.dmnstage.com/teledetection/#product#/#subProduct#/#year#-#month#-#day#/#hour##minute#.#ext#"));
    //
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @RequestMapping(value = "/imagetime/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getImagesTime(@PathVariable Integer id) {
        SubProduct subProduct = service.getSubProductById(id);
        JSONObject obj = new JSONObject();
        obj.put("startTime", subProduct.getStartTime());
        obj.put("endTime", subProduct.getEndTime());
        obj.put("step", subProduct.getStep());
        System.out.println(obj.toString());
        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @RequestMapping(value = "/imagetime2/{id}", method = RequestMethod.GET, produces = "application/json")
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

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET, produces = "application/json")
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


    // **************************
//    //
//    //SELLECT
//    //
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/getuser/{id}", method = RequestMethod.GET)
//    public User getUser(@PathVariable Integer id) {
//        return service.getUserById(id);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/getadmins", method = RequestMethod.GET)
//    public List<Admin> getAdmins() {
//        return service.getAllAdmins();
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/getclients", method = RequestMethod.GET)
//    public List<Client> getClients() {
//        return service.getAllClients();
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
//    @RequestMapping(value = "/getproduct/{id}", method = RequestMethod.GET)
//    public Product getProduct(@PathVariable Integer id) {
//        return service.getProductById(id);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
//    @RequestMapping(value = "/getproducts", method = RequestMethod.GET)
//    public List<Product> getProducts() {
//        return service.getAllProducts();
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
//    @RequestMapping(value = "/getsubproduct/{id}", method = RequestMethod.GET)
//    public SubProduct getSubProduct(@PathVariable Integer id) {
//        return service.getSubProductById(id);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
//    @RequestMapping(value = "/getsubproducts", method = RequestMethod.GET)
//    public List<SubProduct> getSubProducts() {
//        return service.getAllSubProducts();
//    }
//
//    //
//    //CREATE
//    //
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/newadmin", method = RequestMethod.POST)
//    public ResponseEntity<?> newAdmin(@RequestBody Admin admin) {
//
//        if (service.getUserByUsername(admin.getUsername()) == null) {
//            return new ResponseEntity<>(service.newUser(admin), HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
//    }
//    /**
//     * {
//     * "client": {
//     * "username": "b",
//     * "password": "d",
//     * "email": "d",
//     * "phone": "d",
//     * "organizationName": "f"
//     * },
//     * "selectedSubProduct": [
//     * 1,
//     * 2,
//     * 3
//     * ]
//     * }
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/newclient", method = RequestMethod.POST)
//    public ResponseEntity<?> newClient(@RequestBody String jsonStr) {
//
//        JSONObject jObject = new JSONObject(jsonStr);
//        JSONObject clientJson = jObject.getJSONObject("client");
//        if (service.getUserByUsername(clientJson.getString("username")) == null) {
//            Client client = new Client();
//            client.setUsername(clientJson.getString("username"));
//            client.setPassword(clientJson.getString("password"));
//            client.setEmail(clientJson.getString("email"));
//            client.setPhone(clientJson.getString("phone"));
//            client.setOrganizationName(clientJson.getString("organizationName"));
//
//            JSONArray subProductJsonArray = jObject.getJSONArray("selectedSubProduct");
//
//            SubProduct subProduct;
//            for (int i = 0; i < subProductJsonArray.length(); i++) {
//                subProduct = service.getSubProductById(subProductJsonArray.getInt(i));
//                service.mergeClientSubProduct(client, subProduct);
//            }
//
//            return new ResponseEntity<>(service.newUser(client), HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/newproduct", method = RequestMethod.POST)
//    public Product newProduct(@RequestBody Product product) {
//        return service.newProduct(product);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/newsubproduct/{id}", method = RequestMethod.POST)
//    public SubProduct newSubProduct(@RequestBody SubProduct subProduct, @PathVariable(name = "id") Integer selectedProduct) {
//        Product product = service.getProductById(selectedProduct);
//        service.addSubProductToProduct(subProduct, product);
//        return service.newSubProduct(subProduct);
//    }
//
//    //
//    //UPDATE
//    //
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/setconfig", method = RequestMethod.PUT)
//    public ResponseEntity<?> setConfig(@RequestBody Config config) {
//        config.setKey("pathFormat");
//        return new ResponseEntity<>(service.setConfig(config), HttpStatus.OK);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/setadmin/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<?> setAdmin(@RequestBody Admin admin, @PathVariable Integer id) {
//
//        User userInDB = service.getUserById(id);
//        if (userInDB.getUsername().equals(admin.getUsername()) ||
//                (!userInDB.getUsername().equals(admin.getUsername()) && service.getUserByUsername(admin.getUsername()) == null)) {
//            if (admin.getPassword() == null || (admin.getPassword() != null && admin.getPassword().isEmpty()))
//                admin.setPassword(userInDB.getPassword());
//            else {
//                //Hashing
//                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
//            }
//            admin.setId(id);
//            return new ResponseEntity<>(service.setAdmin(admin), HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/setclient/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<?> setClient(@RequestBody String jsonStr, @PathVariable Integer id) {
//
//        JSONObject jObject = new JSONObject(jsonStr);
//        JSONObject clientJson = jObject.getJSONObject("client");
//        Client clientForm = new Client();
//        clientForm.setUsername(clientJson.getString("username"));
//        clientForm.setPassword(clientJson.getString("password"));
//        clientForm.setEmail(clientJson.getString("email"));
//        clientForm.setPhone(clientJson.getString("phone"));
//        clientForm.setOrganizationName(clientJson.getString("organizationName"));
//
//        //Getting user from DB
//        User clientInDB = service.getUserById(id);
//        // Checking if the username hasn't been changed or it already exist
//        if (clientInDB.getUsername().equals(clientForm.getUsername()) ||
//                (!clientInDB.getUsername().equals(clientForm.getUsername()) &&
//                        service.getUserByUsername(clientForm.getUsername()) == null)) {
//            //Checking if the password from the Form is empty
//            if (clientForm.getPassword() == null || (clientForm.getPassword() != null && clientForm.getPassword().isEmpty()))
//                clientForm.setPassword(clientInDB.getPassword());//if it's empty, get the password from the DB
//            else {
//                //Hashing
//                clientForm.setPassword(passwordEncoder.encode(clientForm.getPassword()));//If not hash the new password
//            }
//            clientForm.setId(id);
//
//            JSONArray subProductJsonArray = jObject.getJSONArray("selectedSubProduct");
//
//            SubProduct subProduct;
//            for (int i = 0; i < subProductJsonArray.length(); i++) {
//                subProduct = service.getSubProductById(subProductJsonArray.getInt(i));
//                service.mergeClientSubProduct(clientForm, subProduct);
//            }
//            return new ResponseEntity<>(service.setClient(clientForm), HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Ce nom d'utilisateur existe deja", HttpStatus.BAD_REQUEST);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/setproduct/{id}", method = RequestMethod.PUT)
//    public Product newProduct(@RequestBody Product product, @PathVariable Integer id) {
//        product.setId(id);
//        return service.setProduct(product);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/setsubproduct/{subProductId}/{productId}", method = RequestMethod.PUT)
//    public SubProduct setSubProduct(@RequestBody SubProduct subProduct, @PathVariable Integer subProductId, @PathVariable Integer productId) {
//        subProduct.setId(subProductId);
//        Product product = service.getProductById(productId);
//        service.addSubProductToProduct(subProduct, product);
//        return service.setSubProduct(subProduct);
//    }
//
//    //
//    //DELETE
//    //
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.DELETE)
//    public void deleteUser(@PathVariable Integer id) {
//        service.deleteUser(id);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/deleteproduct/{id}", method = RequestMethod.DELETE)
//    public void deleteProduct(@PathVariable Integer id) {
//        service.deleteProduct(id);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value = "/deletesubproduct/{id}", method = RequestMethod.DELETE)
//    public void deleteSubProduct(@PathVariable Integer id) {
//        service.deleteSubProduct(id);
//    }
//
//    //
//    // service.setConfig(new Config("pathFormat", "http://img.dmnstage.com/teledetection/#product#/#subProduct#/#year#-#month#-#day#/#hour##minute#.#ext#"));
//    //
//
//    @PreAuthorize("hasRole('ROLE_CLIENT')")
//    @RequestMapping(value = "/getimagestime/{id}", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity<?> getImagesTime(@PathVariable Integer id) {
//        SubProduct subProduct = service.getSubProductById(id);
//        JSONObject obj = new JSONObject();
//        obj.put("startTime", subProduct.getStartTime());
//        obj.put("endTime", subProduct.getEndTime());
//        obj.put("step", subProduct.getStep());
//        System.out.println(obj.toString());
//        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
//    }
//
//    @PreAuthorize("hasRole('ROLE_CLIENT')")
//    @RequestMapping(value = "/getimagestime2/{id}", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity<?> getImagesTime2(@PathVariable Integer id) {
//        SubProduct subProduct = service.getSubProductById(id);
//        JSONObject jsonObject = new JSONObject();
//        LocalTime iteratorTime = subProduct.getStartTime();
//        do {
//            jsonObject.accumulate("imageTime", iteratorTime);
//            iteratorTime = iteratorTime.plusMinutes(subProduct.getStep());
//        }
//        while (!iteratorTime.equals(subProduct.getEndTime().plusMinutes(subProduct.getStep())));
//
//        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
//    }
//
//    @PreAuthorize("hasRole('ROLE_CLIENT')")
//    @RequestMapping(value = "/getimage/{id}", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity<?> getImages(@PathVariable Integer id,
//                                       @RequestParam String year,
//                                       @RequestParam String month,
//                                       @RequestParam String day,
//                                       @RequestParam(required = false) String hour,
//                                       @RequestParam(required = false) String minute
//    ) {
//        SubProduct subProduct = service.getSubProductById(id);
//        String url = service.getConfigByKey("pathFormat").getValue();
//
//        url = url.replace("#product#", subProduct.getProduct().getPathName());
//        url = url.replace("#subProduct#", subProduct.getPathName());
//        url = url.replace("#year#", year);
//        url = url.replace("#month#", month);
//        url = url.replace("#day#", day);
//        url = url.replace("#ext#", subProduct.getExt());
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("subProductId", subProduct.getId());
//        jsonObject.put("subProductName", subProduct.getName());
//
//        if (hour != null && minute != null) {
//
//            url = url.replace("#hour#", hour);
//            url = url.replace("#minute#", minute);
//
//            jsonObject.put("img", url);
//
//            HttpURLConnection.setFollowRedirects(false);
//            try {
//                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
//                httpURLConnection.setRequestMethod("HEAD");
//
//                jsonObject.put("httpStatus", httpURLConnection.getResponseCode());
//            } catch (IOException e) {
//                //e.printStackTrace();
//                return new ResponseEntity<>("IOException", HttpStatus.NOT_FOUND);
//            }
//
//            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
//        } else {
//
//            LocalTime iteratorTime = subProduct.getStartTime();
//            String tempHour, tempMinute;
//
//            do {
//                if (iteratorTime.getHour() < 10)
//                    tempHour = "0" + String.valueOf(iteratorTime.getHour());
//                else
//                    tempHour = String.valueOf(iteratorTime.getHour());
//
//                if (iteratorTime.getMinute() < 10)
//                    tempMinute = "0" + String.valueOf(iteratorTime.getMinute());
//                else
//                    tempMinute = String.valueOf(iteratorTime.getMinute());
//
//                url = url.replace("#hour##minute#", tempHour + tempMinute);
//                jsonObject.accumulate("img", url);
//                url = url.replace(tempHour + tempMinute, "#hour##minute#");
//
//                iteratorTime = iteratorTime.plusMinutes(subProduct.getStep());
//            }
//            while (!iteratorTime.equals(subProduct.getEndTime().plusMinutes(subProduct.getStep())));
//
//            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
//        }
//    }
}
