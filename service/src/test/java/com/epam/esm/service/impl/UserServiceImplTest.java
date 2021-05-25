package com.epam.esm.service.impl;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.user.UserLoginAlreadyExistsException;
import com.epam.esm.exception.user.UserNotFoundException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.UserDto;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private final static User USER = new User(1, "loginOne", null, null, null, null);
    private final static UserDto USER_DTO = new UserDto(1, "loginOne", null, null, null, null);

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserMapper userMapper = Mockito.mock(UserMapper.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    private final UserServiceImpl userService =
            new UserServiceImpl(userRepository, userMapper, passwordEncoder);

    @Test
    public void testFindByIdShouldReturnUser() {
        when(userRepository.findById(0)).thenReturn(Optional.of(USER));
        when(userMapper.entityToDto(USER)).thenReturn(USER_DTO);
        UserDto actual = userService.findById(0);
        Assertions.assertEquals(new UserDto(1, "loginOne", null), actual);
    }

    @Test
    public void testFindByIdShouldThrowUserNotFoundExceptionWhenDatabaseNotContainUserId() {
        when(userRepository.findById(0)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.findById(0));
    }

    @Test
    public void testSaveShouldThrowExceptionWhenLoginAlreadyExists() {
        when(userRepository.findByLogin("loginOne")).thenReturn(Optional.of(new User()));
        Assertions.assertThrows(UserLoginAlreadyExistsException.class,
                () -> userService.save(USER_DTO));
    }

    @Test
    public void testSaveShouldSaveUser() {
        User user = new User(1, "login", "pass", "", "", Arrays.asList(Role.USER));
        when(userRepository.findByLogin("loginOne")).thenReturn(Optional.empty());
        when(userMapper.dtoToEntity(USER_DTO)).thenReturn(user);
        when(passwordEncoder.encode(null)).thenReturn("111");
        when(userRepository.saveAndFlush(user)).thenReturn(user);
        UserDto userDto = new UserDto(1, "loginOne", "111", "", "", Arrays.asList(Role.USER));
        when(userMapper.entityToDto(user)).thenReturn(userDto);
        UserDto actual = userService.save(USER_DTO);
        Assertions.assertEquals(
                new UserDto(1, "loginOne", "111", "", "", Arrays.asList(Role.USER)), actual);
    }

}