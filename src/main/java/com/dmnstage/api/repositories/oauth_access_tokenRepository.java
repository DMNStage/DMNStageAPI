package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.oauth_access_token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface oauth_access_tokenRepository extends JpaRepository<oauth_access_token, String> {
}
