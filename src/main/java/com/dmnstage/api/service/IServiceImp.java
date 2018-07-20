package com.dmnstage.api.service;

import com.dmnstage.api.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IServiceImp implements IService {
    @Override
    public void newUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsername().toLowerCase());
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

    }

    @Override
    public void saveUser(User user) {

    }
}
