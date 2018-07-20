package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.SubProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubProductRepository extends JpaRepository<SubProduct, Integer> {

    SubProduct findById(int id);

    SubProduct findByName(String name);
}
