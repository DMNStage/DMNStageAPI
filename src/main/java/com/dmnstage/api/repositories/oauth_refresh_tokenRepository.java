package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.oauth_refresh_token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface oauth_refresh_tokenRepository extends JpaRepository<oauth_refresh_token, Long> {
}
