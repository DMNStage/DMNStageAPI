package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, String> {

    Config findBykey(String key);

}
