package com.dmnstage.api.web;

import com.dmnstage.api.entities.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestService {

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.GET)
    public User getStudent(@PathVariable Integer id) {

        return null; //studentRepository.findById(id).get()
    }

    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    public List<User> getStudents() {

        return null; //studentRepository.findAll()
    }


}
