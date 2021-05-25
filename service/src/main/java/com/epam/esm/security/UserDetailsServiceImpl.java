package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.UserDto;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsCreator userDetailsCreator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserDetailsServiceImpl(UserDetailsCreator userDetailsCreator,
                                  UserRepository userRepository,
                                  UserMapper userMapper) {
        this.userDetailsCreator = userDetailsCreator;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByLogin(login);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException(login));
        UserDto userDto = userMapper.entityToDto(user);
        return userDetailsCreator.create(userDto);
    }

}