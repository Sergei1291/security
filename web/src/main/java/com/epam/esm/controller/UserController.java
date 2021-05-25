package com.epam.esm.controller;

import com.epam.esm.authentication.AuthenticationHelper;
import com.epam.esm.linker.UserControllerLinker;
import com.epam.esm.model.UserDto;
import com.epam.esm.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping(value = "/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserControllerLinker userControllerLinker;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationHelper authenticationHelper,
                          UserControllerLinker userControllerLinker) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userControllerLinker = userControllerLinker;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "constraint.search.page") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Positive(message = "constraint.search.size") int size) {
        Page<UserDto> userPage = userService.findALl(PageRequest.of(page, size));
        return userControllerLinker.addLinksFindAll(userPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findById(
            @PathVariable @Positive(message = "constraint.id") int id) {
        UserDto user = userService.findById(id);
        return userControllerLinker.addLinkOnSelf(user);
    }

    @GetMapping("/himself")
    public ResponseEntity<?> findHimself(Authentication authentication) {
        int userId = authenticationHelper.getUserId(authentication);
        UserDto user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

}