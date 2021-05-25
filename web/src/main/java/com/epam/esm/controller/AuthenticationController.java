package com.epam.esm.controller;

import com.epam.esm.authentication.AuthenticationHelper;
import com.epam.esm.model.AuthenticationRequest;
import com.epam.esm.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/authenticate",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AuthenticationController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return authenticationHelper.login(authenticationRequest);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserDto userDto) {
        return authenticationHelper.signUp(userDto);
    }

}