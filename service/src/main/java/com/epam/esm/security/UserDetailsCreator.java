package com.epam.esm.security;

import com.epam.esm.entity.Role;
import com.epam.esm.model.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsCreator {

    public UserDetails create(UserDto userDto) {
        int id = userDto.getId();
        String login = userDto.getLogin();
        String password = userDto.getPassword();
        Collection<? extends GrantedAuthority> authorities = createAuthorities(userDto);
        return new UserDetailsImpl(id, login, password, authorities);
    }

    private Collection<? extends GrantedAuthority> createAuthorities(UserDto userDto) {
        List<Role> roleList = userDto.getRoles();
        return roleList.stream()
                .map(role -> new SimpleGrantedAuthority(role.name().trim()))
                .collect(Collectors.toList());
    }

}