package com.dmnstage.api.web;

import com.dmnstage.api.entities.Admin;
import com.dmnstage.api.entities.User;
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

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.GET)
    public User getAdmin(@PathVariable Integer id) {
        return service.getAdminById(id); //studentRepository.findById(id).get()
    }

    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    public List<Admin> getAdmins() {
        return service.getAllAdmins(); //studentRepository.findAll()
    }


}
