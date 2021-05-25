package com.epam.esm.linker;

import com.epam.esm.controller.UserController;
import com.epam.esm.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserControllerLinker {

    private final static String FIRST_PAGE = "firstPage";
    private final static String PREVIOUS_PAGE = "previousPage";
    private final static String NEXT_PAGE = "nextPage";
    private final static String LAST_PAGE = "lastPage";

    public ResponseEntity<UserDto> addLinkOnSelf(UserDto user) {
        int id = user.getId();
        Link link = linkTo(methodOn(UserController.class)
                .findById(id))
                .withSelfRel();
        user.add(link);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<CollectionModel<UserDto>> addLinksFindAll(Page<UserDto> userPage) {
        List<UserDto> userList = userPage.getContent();
        userList.forEach(this::addLinkOnSelf);
        int page = userPage.getNumber();
        int size = userPage.getSize();
        Link allUsersLink = linkTo(methodOn(UserController.class)
                .findAll(page, size))
                .withSelfRel();
        Link firstPageLink = linkTo(methodOn(UserController.class)
                .findAll(0, size))
                .withRel(FIRST_PAGE);
        CollectionModel<UserDto> collectionModel =
                CollectionModel.of(userList, allUsersLink, firstPageLink);
        if (userPage.hasPrevious()) {
            Link previousPageLink = linkTo(methodOn(UserController.class)
                    .findAll(page - 1, size))
                    .withRel(PREVIOUS_PAGE);
            collectionModel.add(previousPageLink);
        }
        if (userPage.hasNext()) {
            Link nextPageLink = linkTo(methodOn(UserController.class)
                    .findAll(page + 1, size))
                    .withRel(NEXT_PAGE);
            collectionModel.add(nextPageLink);
        }
        int lastPage = userPage.getTotalPages() - 1;
        if (lastPage != 0) {
            Link lastPageLink = linkTo(methodOn(UserController.class)
                    .findAll(lastPage, size))
                    .withRel(LAST_PAGE);
            collectionModel.add(lastPageLink);
        }
        return ResponseEntity.ok(collectionModel);
    }

}