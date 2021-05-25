package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateIsDeletedException;
import com.epam.esm.exception.certificate.CertificateNameAlreadyExistsException;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.helper.UpdateHelper;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.api.GiftCertificateService;
import com.epam.esm.sort.GiftCertificateSortFactory;
import com.epam.esm.specification.GiftCertificateSpecification;
import com.epam.esm.specification.GiftCertificateSpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateMapper giftCertificateMapper;
    private final TagRepository tagRepository;
    private final UpdateHelper<GiftCertificate> updateHelper;
    private final GiftCertificateSortFactory sortFactory;
    private final GiftCertificateSpecification giftCertificateSpecification;
    private final GiftCertificateSpecificationFactory specificationFactory;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository,
                                      GiftCertificateMapper giftCertificateMapper,
                                      TagRepository tagRepository,
                                      UpdateHelper<GiftCertificate> updateHelper,
                                      GiftCertificateSortFactory sortFactory,
                                      GiftCertificateSpecification giftCertificateSpecification,
                                      GiftCertificateSpecificationFactory specificationFactory) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.giftCertificateMapper = giftCertificateMapper;
        this.tagRepository = tagRepository;
        this.updateHelper = updateHelper;
        this.sortFactory = sortFactory;
        this.giftCertificateSpecification = giftCertificateSpecification;
        this.specificationFactory = specificationFactory;
    }

    @Override
    public Page<GiftCertificateDto> findALl(Pageable pageable) {
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAll(pageable);
        return giftCertificatePage.map(giftCertificateMapper::entityToDto);
    }

    @Override
    public GiftCertificateDto findById(int id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        GiftCertificate giftCertificate = optionalGiftCertificate.orElseThrow(
                () -> new CertificateNotFoundException("" + id));
        return giftCertificateMapper.entityToDto(giftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) {
        String certificateName = giftCertificateDto.getName();
        Optional<GiftCertificate> optionalGiftCertificate =
                giftCertificateRepository.findByName(certificateName);
        if (optionalGiftCertificate.isPresent()) {
            throw new CertificateNameAlreadyExistsException(certificateName);
        }
        GiftCertificate giftCertificate = giftCertificateMapper.dtoToEntity(giftCertificateDto);
        List<Tag> tags = giftCertificate.getTags();
        List<Tag> savedTags = saveTags(tags);
        giftCertificate.setTags(savedTags);
        GiftCertificate savedGiftCertificate = giftCertificateRepository.saveAndFlush(giftCertificate);
        return giftCertificateMapper.entityToDto(savedGiftCertificate);
    }

    private List<Tag> saveTags(List<Tag> tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        List<Tag> savedTags = new ArrayList<>();
        for (Tag tag : tags) {
            String tagName = tag.getName();
            if (tagName != null && !tagName.isEmpty()) {
                Optional<Tag> optionalTag = tagRepository.findByName(tagName);
                Tag tagSaved = optionalTag.orElseGet(() -> tagRepository.saveAndFlush(tag));
                savedTags.add(tagSaved);
            }
        }
        return savedTags.stream().distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto giftCertificateDto) {
        int giftCertificateDtoId = giftCertificateDto.getId();
        Optional<GiftCertificate> optionalGiftCertificateById =
                giftCertificateRepository.findById(giftCertificateDtoId);
        GiftCertificate giftCertificate = optionalGiftCertificateById.orElseThrow(
                () -> new CertificateNotFoundException("" + giftCertificateDtoId));
        boolean isDeleted = giftCertificate.getIsDeleted();
        if (isDeleted) {
            throw new CertificateIsDeletedException("" + giftCertificateDtoId);
        }
        String certificateName = giftCertificateDto.getName();
        Optional<GiftCertificate> optionalGiftCertificateByName =
                giftCertificateRepository.findByName(certificateName);
        if (optionalGiftCertificateByName.isPresent()) {
            throw new CertificateNameAlreadyExistsException(certificateName);
        }
        GiftCertificate giftCertificateUpdated =
                giftCertificateMapper.dtoToEntity(giftCertificateDto);
        List<Tag> tags = giftCertificateUpdated.getTags();
        List<Tag> savedTags = saveTags(tags);
        giftCertificateUpdated.setTags(savedTags);
        GiftCertificate existingCertificate = optionalGiftCertificateById.get();
        updateHelper.update(giftCertificateUpdated, existingCertificate);
        return giftCertificateMapper.entityToDto(existingCertificate);
    }

    @Override
    @Transactional
    public void remove(int id) {
        Optional<GiftCertificate> optionalGiftCertificate =
                giftCertificateRepository.findById(id);
        GiftCertificate giftCertificate = optionalGiftCertificate.orElseThrow(
                () -> new CertificateNotFoundException("" + id));
        boolean isDeleted = giftCertificate.getIsDeleted();
        if (isDeleted) {
            throw new CertificateIsDeletedException("" + id);
        }
        giftCertificate.setIsDeleted(true);
    }

    @Override
    @Transactional
    public Page<GiftCertificateDto> search(List<String> tagNames,
                                           String searchString,
                                           String searchParamName,
                                           String sortParamName,
                                           boolean isOrderDesc,
                                           int page,
                                           int size) {
        Specification<GiftCertificate> specificationSearchParamName =
                specificationFactory.create(searchParamName, searchString);
        Specification<GiftCertificate> specificationTagNames =
                giftCertificateSpecification.hasTagNamesAndCondition(tagNames);
        Sort sort = sortFactory.create(isOrderDesc, sortParamName);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAll(
                specificationSearchParamName.and(specificationTagNames),
                pageable);
        return giftCertificatePage.map(giftCertificateMapper::entityToDto);
    }

    @Override
    @Transactional
    public GiftCertificateDto updatePrice(int idCertificate, int price) {
        Optional<GiftCertificate> optionalGiftCertificate =
                giftCertificateRepository.findById(idCertificate);
        GiftCertificate giftCertificate = optionalGiftCertificate.orElseThrow(
                () -> new CertificateNotFoundException("" + idCertificate));
        boolean isDeleted = giftCertificate.getIsDeleted();
        if (isDeleted) {
            throw new CertificateIsDeletedException("" + idCertificate);
        }
        giftCertificate.setPrice(price);
        return giftCertificateMapper.entityToDto(giftCertificate);
    }

}