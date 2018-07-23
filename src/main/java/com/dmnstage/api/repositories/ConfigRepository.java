package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.Configaasqs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Configaasqs, String> {

    Configaasqs findByKey(String key);

}
