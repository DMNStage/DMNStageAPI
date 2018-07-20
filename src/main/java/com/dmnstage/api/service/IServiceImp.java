package com.dmnstage.api.service;

import com.dmnstage.api.entities.Admin;
import com.dmnstage.api.entities.User;
import com.dmnstage.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IServiceImp implements IService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void newUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

    }

    @Override
    public void saveUser(User user) {

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
