package com.epam.esm.controller;

import com.epam.esm.linker.GiftCertificateControllerLinker;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.service.api.GiftCertificateService;
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

@SpringBootTest(classes = GiftCertificateController.class)
@ExtendWith(SpringExtension.class)
public class GiftCertificateControllerTest {

    @MockBean
    private GiftCertificateService giftCertificateService;
    @MockBean
    private GiftCertificateControllerLinker giftCertificateControllerLinker;
    private MockMvc mockMvc;

    @BeforeEach
    private void initMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new GiftCertificateController(
                        giftCertificateService, giftCertificateControllerLinker))
                .build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(giftCertificateService.findALl(any())).thenReturn(null);
        when(giftCertificateControllerLinker.addLinksFindAll(any()))
                .thenReturn(ResponseEntity.ok(null));
        mockMvc.perform(get("/certificates")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(giftCertificateService, times(1))
                .findALl(any());
        verify(giftCertificateControllerLinker, times(1))
                .addLinksFindAll(any());
    }

    @Test
    public void testFindByIdShouldReturnCorrectStringWhenIdPositive() throws Exception {
        GiftCertificateDto certificateDto = new GiftCertificateDto("certificateName");
        when(giftCertificateService.findById(1)).thenReturn(certificateDto);
        when(giftCertificateControllerLinker.addLinkOnSelf(certificateDto))
                .thenReturn(ResponseEntity.ok(certificateDto));
        MvcResult actual = mockMvc
                .perform(get("/certificates/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"id\":0,\"name\":\"certificateName\"," +
                        "\"description\":null,\"price\":0,\"duration\":0," +
                        "\"createDate\":null,\"lastUpdateDate\":null,\"isDeleted\":false," +
                        "\"tags\":null,\"links\":[]}",
                actual.getResponse().getContentAsString());
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdNegativeOrZero() throws Exception {
        mockMvc.perform(get("/certificates/{id}", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException));
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdNotNumber() throws Exception {
        mockMvc.perform(get("/certificates/{id}", "w1w")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

    @Test
    public void testSaveShouldSaveCertificateWhenCertificateValid() throws Exception {
        GiftCertificateDto certificateDto = new GiftCertificateDto("nameCertificate");
        when(giftCertificateService.save(any())).thenReturn(certificateDto);
        when(giftCertificateControllerLinker.addLinkOnSelf(certificateDto))
                .thenReturn(ResponseEntity.ok(certificateDto));
        mockMvc.perform(post("/certificates")
                .content("{\"name\":\"newTag\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(giftCertificateService, times(1)).save(any());
        verify(giftCertificateControllerLinker, times(1)).addLinkOnSelf(any());
    }

    @Test
    public void testSaveShouldThrowExceptionWhenCertificateInvalid() throws Exception {
        mockMvc.perform(post("/certificates")
                .content("{\"name\":\"\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException));
        verify(giftCertificateService, times(0)).save(any());
        verify(giftCertificateControllerLinker, times(0)).addLinkOnSelf(any());
    }

    @Test
    public void testUpdateShouldUpdate() throws Exception {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        when(giftCertificateService.update(any())).thenReturn(certificateDto);
        when(giftCertificateControllerLinker.addLinkOnSelf(certificateDto))
                .thenReturn(ResponseEntity.ok(certificateDto));
        MvcResult actual = mockMvc.perform(put("/certificates/{id}", "1")
                .content("{\"id\":4,\"name\":\"sws\",\"description\":\"updatedDescr\",\"price\":5}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"id\":0,\"name\":null,\"description\":null," +
                        "\"price\":0,\"duration\":0,\"createDate\":null,\"lastUpdateDate" +
                        "\":null,\"isDeleted\":false,\"tags\":null,\"links\":[]}",
                actual.getResponse().getContentAsString());
    }

    @Test
    public void testRemoveShouldRemoveCertificateWhenIdPositive() throws Exception {
        mockMvc.perform(delete("/certificates/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(giftCertificateService, times(1)).remove(1);
    }

    @Test
    public void testRemoveShouldThrowExceptionWhenIdNegativeOrZero() throws Exception {
        mockMvc.perform(delete("/certificates/{id}", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException));
        verify(giftCertificateService, times(0)).remove(1);
    }

    @Test
    public void testRemoveShouldThrowExceptionWhenIdString() throws Exception {
        mockMvc.perform(delete("/certificates/{id}", "string")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
        verify(giftCertificateService, times(0)).remove(1);
    }

    @Test
    public void testUpdatePriceShouldUpdatePrice() throws Exception {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        when(giftCertificateService.updatePrice(1, 33))
                .thenReturn(certificateDto);
        when(giftCertificateControllerLinker.addLinkOnSelf(certificateDto))
                .thenReturn(ResponseEntity.ok(certificateDto));
        MvcResult actual = mockMvc.perform(patch("/certificates/{id}", "1")
                .param("price", "33")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"id\":0,\"name\":null,\"description\":null," +
                        "\"price\":0,\"duration\":0,\"createDate\":null,\"lastUpdateDate" +
                        "\":null,\"isDeleted\":false,\"tags\":null,\"links\":[]}",
                actual.getResponse().getContentAsString());
    }

}