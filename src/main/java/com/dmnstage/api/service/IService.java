package com.dmnstage.api.service;

import com.dmnstage.api.entities.User;

public interface IService {

    void newUser(User user);

    void saveUser(User user);
}
