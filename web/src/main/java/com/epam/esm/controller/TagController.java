package com.epam.esm.controller;

import com.epam.esm.linker.TagControllerLinker;
import com.epam.esm.model.TagDto;
import com.epam.esm.service.api.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping(value = "/tags",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;
    private final TagControllerLinker tagControllerLinker;

    @Autowired
    public TagController(TagService tagService,
                         TagControllerLinker tagControllerLinker) {
        this.tagService = tagService;
        this.tagControllerLinker = tagControllerLinker;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "constraint.search.page") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Positive(message = "constraint.search.size") int size) {
        Page<TagDto> tagPage = tagService.findALl(PageRequest.of(page, size));
        return tagControllerLinker.addLinksFindAll(tagPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(
            @PathVariable @Positive(message = "constraint.id") int id) {
        TagDto tag = tagService.findById(id);
        return tagControllerLinker.addLinkOnSelf(tag);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> save(@RequestBody @Valid TagDto tag) {
        TagDto savedTag = tagService.save(tag);
        return tagControllerLinker.addLinkOnSelf(savedTag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void remove(@PathVariable @Positive(message = "constraint.id") int id) {
        tagService.remove(id);
    }

    @GetMapping("/mostWidelyUsed")
    public ResponseEntity<?> findMostWidelyUsedTagUserMaxOrderSum() {
        TagDto tag = tagService.findMostWidelyUsedTagUserMaxOrderSum();
        return tagControllerLinker.addLinkOnSelf(tag);
    }

}