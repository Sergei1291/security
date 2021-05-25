package com.epam.esm.linker;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.model.GiftCertificateDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateControllerLinker {

    private final static String FIRST_PAGE = "firstPage";
    private final static String PREVIOUS_PAGE = "previousPage";
    private final static String NEXT_PAGE = "nextPage";
    private final static String LAST_PAGE = "lastPage";

    public ResponseEntity<GiftCertificateDto> addLinkOnSelf(GiftCertificateDto certificate) {
        int id = certificate.getId();
        Link link = linkTo(methodOn(GiftCertificateController.class)
                .findById(id))
                .withSelfRel();
        certificate.add(link);
        return ResponseEntity.ok(certificate);
    }

    public ResponseEntity<CollectionModel<GiftCertificateDto>> addLinksFindAll(
            Page<GiftCertificateDto> giftCertificatePage) {
        List<GiftCertificateDto> certificateList = giftCertificatePage.getContent();
        certificateList.forEach(this::addLinkOnSelf);
        int page = giftCertificatePage.getNumber();
        int size = giftCertificatePage.getSize();
        Link allCertificatesLink = linkTo(methodOn(GiftCertificateController.class)
                .findAll(page, size))
                .withSelfRel();
        Link firstPageLink = linkTo(methodOn(GiftCertificateController.class)
                .findAll(0, size))
                .withRel(FIRST_PAGE);
        CollectionModel<GiftCertificateDto> collectionModel =
                CollectionModel.of(certificateList, allCertificatesLink, firstPageLink);
        if (giftCertificatePage.hasPrevious()) {
            Link previousPageLink = linkTo(methodOn(GiftCertificateController.class)
                    .findAll(page - 1, size))
                    .withRel(PREVIOUS_PAGE);
            collectionModel.add(previousPageLink);
        }
        if (giftCertificatePage.hasNext()) {
            Link nextPageLink = linkTo(methodOn(GiftCertificateController.class)
                    .findAll(page + 1, size))
                    .withRel(NEXT_PAGE);
            collectionModel.add(nextPageLink);
        }
        int lastPage = giftCertificatePage.getTotalPages() - 1;
        if (lastPage != 0) {
            Link lastPageLink = linkTo(methodOn(GiftCertificateController.class)
                    .findAll(lastPage, size))
                    .withRel(LAST_PAGE);
            collectionModel.add(lastPageLink);
        }
        return ResponseEntity.ok(collectionModel);
    }

    public ResponseEntity<CollectionModel<GiftCertificateDto>> addLinksSearch(
            Page<GiftCertificateDto> giftCertificatePage,
            List<String> tagNames,
            String part, String nameSearchParam,
            String nameSortParam, boolean orderDesc) {
        List<GiftCertificateDto> certificateList = giftCertificatePage.getContent();
        certificateList.forEach(this::addLinkOnSelf);
        int page = giftCertificatePage.getNumber();
        int size = giftCertificatePage.getSize();
        Link allCertificatesLink = linkTo(methodOn(GiftCertificateController.class)
                .search(tagNames, part, nameSearchParam, nameSortParam, orderDesc, page, size))
                .withSelfRel();
        Link firstPageLink = linkTo(methodOn(GiftCertificateController.class)
                .search(tagNames, part, nameSearchParam, nameSortParam, orderDesc, 0, size))
                .withRel(FIRST_PAGE);
        CollectionModel<GiftCertificateDto> collectionModel =
                CollectionModel.of(certificateList, allCertificatesLink, firstPageLink);
        if (giftCertificatePage.hasPrevious()) {
            Link previousPageLink = linkTo(methodOn(GiftCertificateController.class)
                    .search(tagNames, part, nameSearchParam, nameSortParam, orderDesc, page - 1, size))
                    .withRel(PREVIOUS_PAGE);
            collectionModel.add(previousPageLink);
        }
        if (giftCertificatePage.hasNext()) {
            Link nextPageLink = linkTo(methodOn(GiftCertificateController.class)
                    .search(tagNames, part, nameSearchParam, nameSortParam, orderDesc, page + 1, size))
                    .withRel(NEXT_PAGE);
            collectionModel.add(nextPageLink);
        }
        int lastPage = giftCertificatePage.getTotalPages() - 1;
        if (lastPage != 0) {
            Link lastPageLink = linkTo(methodOn(GiftCertificateController.class)
                    .search(tagNames, part, nameSearchParam, nameSortParam, orderDesc, lastPage, size))
                    .withRel(LAST_PAGE);
            collectionModel.add(lastPageLink);
        }
        return ResponseEntity.ok(collectionModel);
    }

}