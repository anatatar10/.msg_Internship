package com.calypso.binar.service;

import com.calypso.binar.model.Role;
import com.calypso.binar.repository.RoleRepository;
import com.calypso.binar.service.exception.RoleDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findRoleByRoleName(String roleName) throws RoleDoesNotExistException {
        Optional<Role> role = roleRepository.findByRoleName(roleName);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new RoleDoesNotExistException(roleName);

        }
    }
}
