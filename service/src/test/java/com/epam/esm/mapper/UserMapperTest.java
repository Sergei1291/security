package com.epam.esm.mapper;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.model.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = UserMapper.class)
public class UserMapperTest {

    private final static List<Role> ROLES = Arrays.asList(Role.ADMIN, Role.USER);
    private final static User USER = new User(1, "login", "pass", "name", "surn", ROLES);
    private final static UserDto USER_DTO = new UserDto(1, "login", "pass", "name", "surn", ROLES);

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void testDtoToEntityShouldTransformDtoToEntity() {
        User actual = userMapper.dtoToEntity(USER_DTO);
        Assertions.assertEquals(USER, actual);
    }

    @Test
    public void testEntityToDtoShouldTransformEntityToDto() {
        UserDto actual = userMapper.entityToDto(USER);
        Assertions.assertEquals(USER_DTO, actual);
    }

}