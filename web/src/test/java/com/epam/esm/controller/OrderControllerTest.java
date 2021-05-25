package com.epam.esm.controller;

import com.epam.esm.authentication.AuthenticationHelper;
import com.epam.esm.linker.OrderControllerLinker;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.model.OrderDto;
import com.epam.esm.service.api.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrderController.class)
@ExtendWith(SpringExtension.class)
public class OrderControllerTest {

    @MockBean
    private OrderService orderService;
    @MockBean
    private AuthenticationHelper authenticationHelper;
    @MockBean
    private OrderControllerLinker orderControllerLinker;
    private MockMvc mockMvc;

    @BeforeEach
    private void initMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OrderController(orderService,
                        orderControllerLinker, authenticationHelper))
                .build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(orderService.findALl(any())).thenReturn(null);
        when(orderControllerLinker.addLinksFindAll(any())).thenReturn(ResponseEntity.ok(null));
        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(orderService, times(1)).findALl(any());
        verify(orderControllerLinker, times(1)).addLinksFindAll(any());
    }

    @Test
    public void testFindByIdShouldReturnCorrectStringWhenIdPositive() throws Exception {
        OrderDto orderDto = new OrderDto();
        when(orderService.findById(1)).thenReturn(orderDto);
        when(orderControllerLinker.addLinkOnSelf(orderDto))
                .thenReturn(ResponseEntity.ok(orderDto));
        MvcResult actual = mockMvc
                .perform(get("/orders/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"id\":0,\"cost\":0,\"purchaseTime\":null,\"links\":[]}",
                actual.getResponse().getContentAsString());
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdNegativeOrZero() throws Exception {
        mockMvc.perform(get("/orders/{id}", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException));
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdNotNumber() throws Exception {
        mockMvc.perform(get("/orders/{id}", "w1w")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

    @Test
    public void testFindOrderByUser() throws Exception {
        int userId = 1;
        when(authenticationHelper.getUserId(any())).thenReturn(userId);
        OrderDto orderDto = new OrderDto();
        when(orderService.findOrderByUser(userId, 22)).thenReturn(orderDto);
        when(orderControllerLinker.addLinkFindOrderByUser(orderDto, null))
                .thenReturn(ResponseEntity.ok(orderDto));
        MvcResult actual = mockMvc
                .perform(get("/orders")
                        .param("orderId", "22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"id\":0,\"cost\":0,\"purchaseTime\":null,\"links\":[]}",
                actual.getResponse().getContentAsString());
    }

    @Test
    public void testFindUserOrdersUser() throws Exception {
        int userId = 1;
        when(authenticationHelper.getUserId(any())).thenReturn(userId);
        PageRequest request = PageRequest.of(0, 10);
        when(orderService.findAllOrdersByUser(userId, request)).thenReturn(null);
        when(orderControllerLinker.addLinksFindUserOrdersRoleUser(any(), any()))
                .thenReturn(ResponseEntity.ok(null));
        mockMvc.perform(get("/orders/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindUserOrdersAdmin() throws Exception {
        int userId = 1;
        PageRequest request = PageRequest.of(0, 10);
        when(orderService.findAllOrdersByUser(userId, request)).thenReturn(null);
        when(orderControllerLinker.addLinksFindUserOrdersRoleAdmin(null, userId))
                .thenReturn(ResponseEntity.ok(null));
        mockMvc.perform(get("/orders")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testMakeOrderCertificate() throws Exception {
        int userId = 1;
        when(authenticationHelper.getUserId(any())).thenReturn(userId);
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        int certificateId = 1;
        certificateDto.setId(certificateId);
        OrderDto orderDto = new OrderDto();
        when(orderService.makeOrderCertificate(userId, certificateDto))
                .thenReturn(orderDto);
        when(orderControllerLinker.addLinkFindOrderByUser(orderDto, null))
                .thenReturn(ResponseEntity.ok(orderDto));
        mockMvc.perform(post("/orders")
                .content("{\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}