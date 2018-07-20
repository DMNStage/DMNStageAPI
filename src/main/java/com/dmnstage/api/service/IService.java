package com.dmnstage.api.service;

import com.dmnstage.api.entities.Admin;
import com.dmnstage.api.entities.User;

import java.util.List;

public interface IService {

    void newUser(User user);

    void saveUser(User user);

    User getAdminById(int id);

    List<Admin> getAllAdmins();
}
