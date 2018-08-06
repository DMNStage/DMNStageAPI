package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.Admin;
import com.dmnstage.api.entities.Client;
import com.dmnstage.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByUsername(String username);

    User findById(int id);

    @Query("select u from User u where TYPE(u) = 'Admin'")
    List<Admin> findAllAdmins();

    @Query("select u from User u where TYPE(u) = 'Client'")
    List<Client> findAllClients();

    @Query("select u from User u where TYPE(u) = 'Admin' AND u.id= :id")
    User findAdminByID(@Param("id") int id);

    @Query("select u from User u where TYPE(u) = 'Client' AND u.id= :id")
    User findClientByID(@Param("id") int id);

}
