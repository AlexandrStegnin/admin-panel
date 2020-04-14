package com.ddkolesnik.adminpanel.repository;

import com.ddkolesnik.adminpanel.model.AppToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AppTokenRepository extends JpaRepository<AppToken, Long> {
}
