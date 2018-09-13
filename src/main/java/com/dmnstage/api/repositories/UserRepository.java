package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByUsername(String username);

    User findById(int id);

    List<User> findAll();

    @Query("select u from User u where TYPE(u) = 'Admin'")
    List<User> findAllAdmins(); //List<Admin> findAllAdmins();

    @Query("select u from User u where TYPE(u) = 'Client'")
    List<User> findAllClients(); //List<Client> findAllClients();

    @Query("select u from User u where TYPE(u) = 'Admin' AND u.id= :id")
    User findAdminByID(@Param("id") int id);

    @Query("select u from User u where TYPE(u) = 'Client' AND u.id= :id")
    User findClientByID(@Param("id") int id);

    @Query(value = "select u.username from user u inner join client_sub_product as s on u.id = s.client_id where s.sub_product_id = ?1", nativeQuery = true)
    List<String> getClientsBySubProduct(int id);
}
