package com.epam.esm.linker;

import com.epam.esm.controller.TagController;
import com.epam.esm.model.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagControllerLinker {

    private final static String FIRST_PAGE = "firstPage";
    private final static String PREVIOUS_PAGE = "previousPage";
    private final static String NEXT_PAGE = "nextPage";
    private final static String LAST_PAGE = "lastPage";

    public ResponseEntity<TagDto> addLinkOnSelf(TagDto tag) {
        int id = tag.getId();
        Link link = linkTo(methodOn(TagController.class).findById(id))
                .withSelfRel();
        tag.add(link);
        return ResponseEntity.ok(tag);
    }

    public ResponseEntity<CollectionModel<TagDto>> addLinksFindAll(Page<TagDto> tagPage) {
        List<TagDto> tagList = tagPage.getContent();
        tagList.forEach(this::addLinkOnSelf);
        int page = tagPage.getNumber();
        int size = tagPage.getSize();
        Link allTagsLink = linkTo(methodOn(TagController.class)
                .findAll(page, size))
                .withSelfRel();
        Link firstPageLink = linkTo(methodOn(TagController.class)
                .findAll(0, size))
                .withRel(FIRST_PAGE);
        CollectionModel<TagDto> collectionModel =
                CollectionModel.of(tagList, allTagsLink, firstPageLink);
        if (tagPage.hasPrevious()) {
            Link previousPageLink = linkTo(methodOn(TagController.class)
                    .findAll(page - 1, size))
                    .withRel(PREVIOUS_PAGE);
            collectionModel.add(previousPageLink);
        }
        if (tagPage.hasNext()) {
            Link nextPageLink = linkTo(methodOn(TagController.class)
                    .findAll(page + 1, size))
                    .withRel(NEXT_PAGE);
            collectionModel.add(nextPageLink);
        }
        int lastPage = tagPage.getTotalPages() - 1;
        if (lastPage != 0) {
            Link lastPageLink = linkTo(methodOn(TagController.class)
                    .findAll(lastPage, size))
                    .withRel(LAST_PAGE);
            collectionModel.add(lastPageLink);
        }
        return ResponseEntity.ok(collectionModel);
    }

}