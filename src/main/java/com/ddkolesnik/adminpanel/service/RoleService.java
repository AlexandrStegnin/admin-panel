package com.ddkolesnik.adminpanel.service;

import com.ddkolesnik.adminpanel.model.Role;
import com.ddkolesnik.adminpanel.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ddkolesnik.adminpanel.configuration.support.Constant.ROLE_USER;


/**
 * @author Alexandr Stegnin
 */

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role create(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role update(Role role) {
        return roleRepository.save(role);
    }

    public void delete(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    public Role getDefaultUserRole() {
        return findByName(ROLE_USER);
    }

    public Long count() {
        return roleRepository.count();
    }
}
