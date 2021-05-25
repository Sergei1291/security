package com.epam.esm.model;

import com.epam.esm.entity.Role;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class UserDto extends RepresentationModel<UserDto> implements Serializable {

    private int id;
    @NotEmpty(message = "constraint.user.login")
    @Pattern(message = "constraint.user.login.pattern", regexp = "[A-Za-z0-9]{8,20}")
    private String login;
    @NotEmpty(message = "constraint.user.password")
    @Pattern(message = "constraint.user.password.pattern",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")
    private String password;
    @NotEmpty(message = "constraint.user.surname")
    private String surname;
    @NotEmpty(message = "constraint.user.name")
    private String name;
    private List<Role> roles;

    public UserDto() {
    }

    public UserDto(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public UserDto(int id, String login, String password, List<Role> roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public UserDto(String login, String password, String name, String surname, List<Role> roles) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
    }

    public UserDto(int id, String login, String password, String name, String surname, List<Role> roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        UserDto user = (UserDto) o;
        return id == user.id
                && Objects.equals(login, user.login)
                && Objects.equals(password, user.password)
                && Objects.equals(surname, user.surname)
                && Objects.equals(name, user.name)
                && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, login, password, surname, name, roles);
    }

}