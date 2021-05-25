package com.epam.esm.controller;

import com.epam.esm.authentication.AuthenticationHelper;
import com.epam.esm.linker.UserControllerLinker;
import com.epam.esm.model.UserDto;
import com.epam.esm.service.api.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationHelper authenticationHelper;
    @MockBean
    private UserControllerLinker userControllerLinker;
    private MockMvc mockMvc;

    @BeforeEach
    private void initMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userService, authenticationHelper, userControllerLinker))
                .build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(userService.findALl(any())).thenReturn(null);
        when(userControllerLinker.addLinksFindAll(any())).thenReturn(ResponseEntity.ok(null));
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService, times(1)).findALl(any());
        verify(userControllerLinker, times(1)).addLinksFindAll(any());
    }

    @Test
    public void testFindByIdShouldReturnCorrectStringWhenIdPositive() throws Exception {
        UserDto userDto = new UserDto(1, "login", "pass");
        when(userService.findById(1)).thenReturn(userDto);
        when(userControllerLinker.addLinkOnSelf(userDto)).thenReturn(ResponseEntity.ok(userDto));
        MvcResult actual = mockMvc
                .perform(get("/users/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"id\":1,\"login\":\"login\",\"password\":\"pass\"," +
                        "\"surname\":null,\"name\":null,\"roles\":null,\"links\":[]}",
                actual.getResponse().getContentAsString());
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdNegativeOrZero() throws Exception {
        mockMvc.perform(get("/users/{id}", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException));
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdNotNumber() throws Exception {
        mockMvc.perform(get("/users/{id}", "s1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

}