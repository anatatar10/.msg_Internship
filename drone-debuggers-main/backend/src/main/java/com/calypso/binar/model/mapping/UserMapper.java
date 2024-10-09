package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.UserDTO;
import com.calypso.binar.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toUserEntity(UserDTO userDTO);


}
