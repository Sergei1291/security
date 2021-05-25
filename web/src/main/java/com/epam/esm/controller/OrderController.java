package com.epam.esm.controller;

import com.epam.esm.authentication.AuthenticationHelper;
import com.epam.esm.linker.OrderControllerLinker;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.model.OrderDto;
import com.epam.esm.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
@RequestMapping(value = "/orders",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderService orderService;
    private final OrderControllerLinker orderControllerLinker;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public OrderController(OrderService orderService,
                           OrderControllerLinker orderControllerLinker,
                           AuthenticationHelper authenticationHelper) {
        this.orderService = orderService;
        this.orderControllerLinker = orderControllerLinker;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "constraint.search.page") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Positive(message = "constraint.search.size") int size) {
        Page<OrderDto> orderPage = orderService.findALl(PageRequest.of(page, size));
        return orderControllerLinker.addLinksFindAll(orderPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findById(
            @PathVariable @Positive(message = "constraint.id") int id) {
        OrderDto order = orderService.findById(id);
        return orderControllerLinker.addLinkOnSelf(order);
    }

    @GetMapping(params = {"orderId"})
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> findOrderByUser(
            @RequestParam @Positive(message = "constraint.id") int orderId,
            Authentication authentication) {
        int userId = authenticationHelper.getUserId(authentication);
        OrderDto order = orderService.findOrderByUser(userId, orderId);
        return orderControllerLinker.addLinkFindOrderByUser(order, authentication);
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> findUserOrders(
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "constraint.search.page") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Positive(message = "constraint.search.size") int size,
            Authentication authentication) {
        int userId = authenticationHelper.getUserId(authentication);
        Page<OrderDto> orderPage = orderService.findAllOrdersByUser(userId, PageRequest.of(page, size));
        return orderControllerLinker.addLinksFindUserOrdersRoleUser(orderPage, authentication);
    }

    @GetMapping(params = {"userId"})
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findUserOrders(
            @RequestParam @Positive(message = "constraint.id") int userId,
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "constraint.search.page") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Positive(message = "constraint.search.size") int size) {
        Page<OrderDto> orderPage =
                orderService.findAllOrdersByUser(userId, PageRequest.of(page, size));
        return orderControllerLinker.addLinksFindUserOrdersRoleAdmin(orderPage, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> makeOrderCertificate(
            @RequestBody GiftCertificateDto certificate,
            Authentication authentication) {
        int userId = authenticationHelper.getUserId(authentication);
        OrderDto order = orderService.makeOrderCertificate(userId, certificate);
        return orderControllerLinker.addLinkFindOrderByUser(order, authentication);
    }

}