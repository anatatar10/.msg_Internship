package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.RoleDTO;
import com.calypso.binar.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toRoleDTO(Role role);

    Role toRoleEntity(RoleDTO roleDTO);
}
