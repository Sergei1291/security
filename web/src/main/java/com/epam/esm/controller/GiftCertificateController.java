package com.epam.esm.controller;

import com.epam.esm.linker.GiftCertificateControllerLinker;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.service.api.GiftCertificateService;
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
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/certificates",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateControllerLinker giftCertificateControllerLinker;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     GiftCertificateControllerLinker giftCertificateControllerLinker) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateControllerLinker = giftCertificateControllerLinker;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "constraint.search.page") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Positive(message = "constraint.search.size") int size) {
        Page<GiftCertificateDto> giftCertificatePage =
                giftCertificateService.findALl(PageRequest.of(page, size));
        return giftCertificateControllerLinker.addLinksFindAll(giftCertificatePage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(
            @PathVariable @Positive(message = "constraint.id") int id) {
        GiftCertificateDto giftCertificate = giftCertificateService.findById(id);
        return giftCertificateControllerLinker.addLinkOnSelf(giftCertificate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> save(@RequestBody @Valid GiftCertificateDto giftCertificate) {
        GiftCertificateDto savedGiftCertificate = giftCertificateService.save(giftCertificate);
        return giftCertificateControllerLinker.addLinkOnSelf(savedGiftCertificate);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable @Positive(message = "constraint.id") int id,
            @RequestBody GiftCertificateDto giftCertificate) {
        giftCertificate.setId(id);
        GiftCertificateDto updatedGiftCertificate = giftCertificateService.update(giftCertificate);
        return giftCertificateControllerLinker.addLinkOnSelf(updatedGiftCertificate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void remove(@PathVariable @Positive(message = "constraint.id") int id) {
        giftCertificateService.remove(id);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false, defaultValue = "") List<String> tagNames,
            @RequestParam(required = false, defaultValue = "") String part,
            @RequestParam(required = false, defaultValue = "name") String nameSearchParam,
            @RequestParam(required = false, defaultValue = "id") String nameSortParam,
            @RequestParam(required = false, defaultValue = "false") boolean orderDesc,
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "constraint.search.page") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Positive(message = "constraint.search.size") int size) {
        Page<GiftCertificateDto> giftCertificatePage = giftCertificateService.search(
                tagNames, part, nameSearchParam, nameSortParam, orderDesc, page, size);
        return giftCertificateControllerLinker.addLinksSearch(giftCertificatePage, tagNames,
                part, nameSearchParam, nameSortParam, orderDesc);
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updatePrice(
            @PathVariable @Positive(message = "constraint.id") int id,
            @RequestParam @PositiveOrZero(message = "constraint.gift.certificate.price") int price) {
        GiftCertificateDto giftCertificate = giftCertificateService.updatePrice(id, price);
        return giftCertificateControllerLinker.addLinkOnSelf(giftCertificate);
    }

}