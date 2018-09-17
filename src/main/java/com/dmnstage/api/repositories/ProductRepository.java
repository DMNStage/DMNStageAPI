package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findById(int id);

    Product findByName(String name);

    @Query(value = "SELECT DISTINCT p.* FROM user AS u INNER JOIN client_sub_product AS csp ON u.id = csp.client_id " +
            "INNER JOIN sub_product AS sp ON csp.sub_product_id = sp.id INNER JOIN product AS p ON sp.product_id = p.id where u.username = ?1", nativeQuery = true)
    List<Product> getProductsByClient(String username);
}