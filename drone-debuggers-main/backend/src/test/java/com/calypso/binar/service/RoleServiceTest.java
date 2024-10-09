package com.calypso.binar.service;

import com.calypso.binar.model.Role;
import com.calypso.binar.repository.RoleRepository;
import com.calypso.binar.service.exception.RoleDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;


    @Test
    public void findRoleByRoleName_returnsRole_whenRoleExists() throws RoleDoesNotExistException {
        // Given
        String roleName = "ADMIN";
        Role role = new Role();
        role.setRoleName(roleName);
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.of(role));

        // When
        Role foundRole = roleService.findRoleByRoleName(roleName);

        // Then
        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getRoleName()).isEqualTo(roleName);
    }

    @Test
    public void findRoleByRoleName_throwsRoleDoesNotExistException_whenRoleDoesNotExist() {
        // Given
        String roleName = "NON_EXISTENT_ROLE";
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());

        // When & Then
        RoleDoesNotExistException thrown = assertThrows(
                RoleDoesNotExistException.class,
                () -> roleService.findRoleByRoleName(roleName)
        );
        assertThat(thrown).hasMessage(roleName + " does not exist");
    }



}
