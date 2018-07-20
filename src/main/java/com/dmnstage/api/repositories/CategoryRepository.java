package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findById(int id);

    Category findByName(String name);

}
