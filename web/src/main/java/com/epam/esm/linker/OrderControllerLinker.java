package com.epam.esm.linker;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.model.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderControllerLinker {

    private final static String FIRST_PAGE = "firstPage";
    private final static String PREVIOUS_PAGE = "previousPage";
    private final static String NEXT_PAGE = "nextPage";
    private final static String LAST_PAGE = "lastPage";
    private final static String CERTIFICATE = "certificate";

    public ResponseEntity<OrderDto> addLinkOnSelf(OrderDto order) {
        int id = order.getId();
        Link link = linkTo(methodOn(OrderController.class)
                .findById(id))
                .withSelfRel();
        order.add(link);
        GiftCertificateDto giftCertificate = order.getGiftCertificate();
        int idCertificate = giftCertificate.getId();
        Link linkCertificate = linkTo(methodOn(GiftCertificateController.class)
                .findById(idCertificate))
                .withRel(CERTIFICATE);
        order.add(linkCertificate);
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<CollectionModel<OrderDto>> addLinksFindAll(
            Page<OrderDto> orderPage) {
        List<OrderDto> orderList = orderPage.getContent();
        orderList.forEach(this::addLinkOnSelf);
        int page = orderPage.getNumber();
        int size = orderPage.getSize();
        Link allOrdersLink = linkTo(methodOn(OrderController.class)
                .findAll(page, size))
                .withSelfRel();
        Link firstPageLink = linkTo(methodOn(OrderController.class)
                .findAll(0, size))
                .withRel(FIRST_PAGE);
        CollectionModel<OrderDto> collectionModel =
                CollectionModel.of(orderList, allOrdersLink, firstPageLink);
        if (orderPage.hasPrevious()) {
            Link previousPageLink = linkTo(methodOn(OrderController.class)
                    .findAll(page - 1, size))
                    .withRel(PREVIOUS_PAGE);
            collectionModel.add(previousPageLink);
        }
        if (orderPage.hasNext()) {
            Link nextPageLink = linkTo(methodOn(OrderController.class)
                    .findAll(page + 1, size))
                    .withRel(NEXT_PAGE);
            collectionModel.add(nextPageLink);
        }
        int lastPage = orderPage.getTotalPages() - 1;
        if (lastPage != 0) {
            Link lastPageLink = linkTo(methodOn(OrderController.class)
                    .findAll(lastPage, size))
                    .withRel(LAST_PAGE);
            collectionModel.add(lastPageLink);
        }
        return ResponseEntity.ok(collectionModel);
    }

    public ResponseEntity<CollectionModel<OrderDto>> addLinksFindUserOrdersRoleUser(
            Page<OrderDto> orderPage, Authentication authentication) {
        List<OrderDto> orderList = orderPage.getContent();
        orderList.forEach(order -> addLinkFindOrderByUser(order, authentication));
        int page = orderPage.getNumber();
        int size = orderPage.getSize();
        Link allOrdersLink = linkTo(methodOn(OrderController.class)
                .findUserOrders(page, size, authentication))
                .withSelfRel();
        Link firstPageLink = linkTo(methodOn(OrderController.class)
                .findUserOrders(0, size, authentication))
                .withRel(FIRST_PAGE);
        CollectionModel<OrderDto> collectionModel =
                CollectionModel.of(orderList, allOrdersLink, firstPageLink);
        if (orderPage.hasPrevious()) {
            Link previousPageLink = linkTo(methodOn(OrderController.class)
                    .findUserOrders(page - 1, size, authentication))
                    .withRel(PREVIOUS_PAGE);
            collectionModel.add(previousPageLink);
        }
        if (orderPage.hasNext()) {
            Link nextPageLink = linkTo(methodOn(OrderController.class)
                    .findUserOrders(page + 1, size, authentication))
                    .withRel(NEXT_PAGE);
            collectionModel.add(nextPageLink);
        }
        int lastPage = orderPage.getTotalPages() - 1;
        if (lastPage != 0) {
            Link lastPageLink = linkTo(methodOn(OrderController.class)
                    .findUserOrders(lastPage, size, authentication))
                    .withRel(LAST_PAGE);
            collectionModel.add(lastPageLink);
        }
        return ResponseEntity.ok(collectionModel);
    }

    public ResponseEntity<CollectionModel<OrderDto>> addLinksFindUserOrdersRoleAdmin(
            Page<OrderDto> orderPage, int userId) {
        List<OrderDto> orderList = orderPage.getContent();
        orderList.forEach(this::addLinkOnSelf);
        int page = orderPage.getNumber();
        int size = orderPage.getSize();
        Link allOrdersLink = linkTo(methodOn(OrderController.class)
                .findUserOrders(userId, page, size))
                .withSelfRel();
        Link firstPageLink = linkTo(methodOn(OrderController.class)
                .findUserOrders(userId, 0, size))
                .withRel(FIRST_PAGE);
        CollectionModel<OrderDto> collectionModel =
                CollectionModel.of(orderList, allOrdersLink, firstPageLink);
        if (orderPage.hasPrevious()) {
            Link previousPageLink = linkTo(methodOn(OrderController.class)
                    .findUserOrders(userId, page - 1, size))
                    .withRel(PREVIOUS_PAGE);
            collectionModel.add(previousPageLink);
        }
        if (orderPage.hasNext()) {
            Link nextPageLink = linkTo(methodOn(OrderController.class)
                    .findUserOrders(userId, page + 1, size))
                    .withRel(NEXT_PAGE);
            collectionModel.add(nextPageLink);
        }
        int lastPage = orderPage.getTotalPages() - 1;
        if (lastPage != 0) {
            Link lastPageLink = linkTo(methodOn(OrderController.class)
                    .findUserOrders(userId, lastPage, size))
                    .withRel(LAST_PAGE);
            collectionModel.add(lastPageLink);
        }
        return ResponseEntity.ok(collectionModel);
    }

    public ResponseEntity<OrderDto> addLinkFindOrderByUser(OrderDto order,
                                                           Authentication authentication) {
        int id = order.getId();
        Link link = linkTo(methodOn(OrderController.class)
                .findOrderByUser(id, authentication))
                .withSelfRel();
        order.add(link);
        GiftCertificateDto giftCertificate = order.getGiftCertificate();
        int idCertificate = giftCertificate.getId();
        Link linkCertificate = linkTo(methodOn(GiftCertificateController.class)
                .findById(idCertificate))
                .withRel(CERTIFICATE);
        order.add(linkCertificate);
        return ResponseEntity.ok(order);
    }

}