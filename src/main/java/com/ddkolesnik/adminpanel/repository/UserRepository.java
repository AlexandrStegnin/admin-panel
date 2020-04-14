package com.ddkolesnik.adminpanel.repository;

import com.ddkolesnik.adminpanel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    void deleteById(Long id);

    long count();

}
