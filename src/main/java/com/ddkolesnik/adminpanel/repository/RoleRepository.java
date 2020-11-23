package com.ddkolesnik.adminpanel.repository;

import com.ddkolesnik.adminpanel.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String title);

    Optional<Role> findById(Long id);

    long count();

}
