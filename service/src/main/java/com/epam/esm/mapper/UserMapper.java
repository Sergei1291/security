package com.epam.esm.mapper;

import com.epam.esm.entity.User;
import com.epam.esm.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToEntity(UserDto userDto);

    UserDto entityToDto(User user);

}