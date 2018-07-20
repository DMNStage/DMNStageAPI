package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findById(int id);

    Product findByName(String name);
}
