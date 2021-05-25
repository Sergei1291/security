package com.epam.esm.controller;

import com.epam.esm.linker.TagControllerLinker;
import com.epam.esm.model.TagDto;
import com.epam.esm.service.api.TagService;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TagController.class)
@ExtendWith(SpringExtension.class)
public class TagControllerTest {

    @MockBean
    private TagService tagService;
    @MockBean
    private TagControllerLinker tagControllerLinker;
    private MockMvc mockMvc;

    @BeforeEach
    private void initMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TagController(tagService, tagControllerLinker))
                .build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(tagService.findALl(any())).thenReturn(null);
        when(tagControllerLinker.addLinksFindAll(any())).thenReturn(ResponseEntity.ok(null));
        mockMvc.perform(get("/tags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(tagService, times(1)).findALl(any());
        verify(tagControllerLinker, times(1)).addLinksFindAll(any());
    }

    @Test
    public void testFindByIdShouldReturnCorrectStringWhenIdPositive() throws Exception {
        TagDto tagDto = new TagDto(1, "tagName");
        when(tagService.findById(1)).thenReturn(tagDto);
        when(tagControllerLinker.addLinkOnSelf(tagDto)).thenReturn(ResponseEntity.ok(tagDto));
        MvcResult actual = mockMvc
                .perform(get("/tags/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"id\":1,\"name\":\"tagName\",\"links\":[]}",
                actual.getResponse().getContentAsString());
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdNegativeOrZero() throws Exception {
        mockMvc.perform(get("/tags/{id}", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException));
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdNotNumber() throws Exception {
        mockMvc.perform(get("/tags/{id}", "w1w")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

    @Test
    public void testSaveShouldSaveTagWhenTagValid() throws Exception {
        TagDto tagDto = new TagDto(1, "tagName");
        when(tagService.save(any())).thenReturn(tagDto);
        when(tagControllerLinker.addLinkOnSelf(tagDto)).thenReturn(ResponseEntity.ok(tagDto));
        mockMvc.perform(post("/tags")
                .content("{\"name\":\"newTag\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(tagService, times(1)).save(any());
        verify(tagControllerLinker, times(1)).addLinkOnSelf(any());
    }

    @Test
    public void testSaveShouldThrowExceptionWhenTagInvalid() throws Exception {
        mockMvc.perform(post("/tags")
                .content("{\"name\":\"\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException));
        verify(tagService, times(0)).save(any());
        verify(tagControllerLinker, times(0)).addLinkOnSelf(any());
    }

    @Test
    public void testRemoveShouldRemoveTagWhenIdPositive() throws Exception {
        mockMvc.perform(delete("/tags/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(tagService, times(1)).remove(1);
    }

    @Test
    public void testRemoveShouldThrowExceptionWhenIdNegativeOrZero() throws Exception {
        mockMvc.perform(delete("/tags/{id}", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException));
        verify(tagService, times(0)).remove(1);
    }

    @Test
    public void testRemoveShouldThrowExceptionWhenIdString() throws Exception {
        mockMvc.perform(delete("/tags/{id}", "-1s")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
        verify(tagService, times(0)).remove(1);
    }

    @Test
    public void testFindMostWidelyUsedTagUserMaxOrderSum() throws Exception {
        TagDto tagDto = new TagDto(1, "tagName");
        when(tagService.findMostWidelyUsedTagUserMaxOrderSum()).thenReturn(tagDto);
        when(tagControllerLinker.addLinkOnSelf(tagDto)).thenReturn(ResponseEntity.ok(tagDto));
        MvcResult actual = mockMvc
                .perform(get("/tags/mostWidelyUsed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"id\":1,\"name\":\"tagName\",\"links\":[]}",
                actual.getResponse().getContentAsString());
    }

}