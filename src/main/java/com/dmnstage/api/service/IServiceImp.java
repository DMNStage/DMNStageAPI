package com.dmnstage.api.service;

import com.dmnstage.api.entities.Admin;
import com.dmnstage.api.entities.User;
import com.dmnstage.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@Transactional
public class IServiceImp implements IService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User newUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsername().toLowerCase());
        //user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public User saveUser(User user) {

        return user;
    }

    @Override
    public User getAdminById(int id) {
        return userRepository.findAdminByID(id);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return userRepository.findAllAdmins();
    }


}
