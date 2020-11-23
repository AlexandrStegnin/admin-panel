package com.ddkolesnik.adminpanel.configuration.security;

import com.ddkolesnik.adminpanel.model.User;
import com.ddkolesnik.adminpanel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author Alexandr Stegnin
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with login '%s'.", username));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), true, true,
                true, true, Collections.singleton(user.getRole()));
    }

}
