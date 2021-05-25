package com.epam.esm.service.impl;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.user.UserLoginAlreadyExistsException;
import com.epam.esm.exception.user.UserNotFoundException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.UserDto;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserDto> findALl(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::entityToDto);
    }

    @Override
    public UserDto findById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("" + id));
        return userMapper.entityToDto(user);
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        String login = userDto.getLogin();
        Optional<User> optionalUser = userRepository.findByLogin(login);
        if (optionalUser.isPresent()) {
            throw new UserLoginAlreadyExistsException(login);
        }
        User user = userMapper.dtoToEntity(userDto);
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        List<Role> roles = Collections.singletonList(Role.USER);
        user.setRoles(roles);
        User savedUser = userRepository.saveAndFlush(user);
        return userMapper.entityToDto(savedUser);
    }

}