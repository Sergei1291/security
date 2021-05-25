package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.certificate.CertificateNameAlreadyExistsException;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.helper.GiftCertificateUpdateHelper;
import com.epam.esm.helper.UpdateHelper;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.api.GiftCertificateService;
import com.epam.esm.sort.GiftCertificateSortFactory;
import com.epam.esm.sort.GiftCertificateSortFactoryImpl;
import com.epam.esm.specification.GiftCertificateSpecification;
import com.epam.esm.specification.GiftCertificateSpecificationFactory;
import com.epam.esm.specification.GiftCertificateSpecificationFactoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceImplTest {

    private final static GiftCertificate GIFT_CERTIFICATE =
            new GiftCertificate(1, "name1", "description1", 1, 1, null, null, false, new ArrayList<>());
    private final static GiftCertificateDto GIFT_CERTIFICATE_DTO =
            new GiftCertificateDto(1, "name1", "description1", 1, 1, null, null, new ArrayList<>());

    private final GiftCertificateRepository giftCertificateRepository =
            Mockito.mock(GiftCertificateRepository.class);
    private final GiftCertificateMapper giftCertificateMapper =
            Mockito.mock(GiftCertificateMapper.class);
    private final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    private final UpdateHelper<GiftCertificate> updateHelper =
            Mockito.mock(GiftCertificateUpdateHelper.class);
    private final GiftCertificateSortFactory sortFactory =
            Mockito.mock(GiftCertificateSortFactoryImpl.class);
    private final GiftCertificateSpecification giftCertificateSpecification =
            Mockito.mock(GiftCertificateSpecification.class);
    private final GiftCertificateSpecificationFactory specificationFactory =
            Mockito.mock(GiftCertificateSpecificationFactoryImpl.class);

    private final GiftCertificateService giftCertificateService =
            new GiftCertificateServiceImpl(
                    giftCertificateRepository,
                    giftCertificateMapper,
                    tagRepository,
                    updateHelper,
                    sortFactory,
                    giftCertificateSpecification,
                    specificationFactory);

    @Test
    public void testFindByIdShouldReturnCertificate() {
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(GIFT_CERTIFICATE));
        when(giftCertificateMapper.entityToDto(GIFT_CERTIFICATE)).thenReturn(GIFT_CERTIFICATE_DTO);
        GiftCertificateDto actual = giftCertificateService.findById(1);
        GiftCertificateDto expected =
                new GiftCertificateDto(1, "name1", "description1", 1, 1, null, null, new ArrayList<>());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenDatabaseNotContainCertificateId() {
        when(tagRepository.findById(0)).thenReturn(Optional.empty());
        Assertions.assertThrows(CertificateNotFoundException.class,
                () -> giftCertificateService.findById(0));
    }

    @Test
    public void testSaveShouldThrowExceptionWhenCertificateNameAlreadyExists() {
        when(giftCertificateRepository.findByName("name1")).thenReturn(Optional.of(new GiftCertificate()));
        Assertions.assertThrows(CertificateNameAlreadyExistsException.class,
                () -> giftCertificateService.save(GIFT_CERTIFICATE_DTO));
    }

    @Test
    public void testSaveShouldReturnSavedCertificate() {
        GiftCertificateDto giftCertificateDto =
                new GiftCertificateDto("name1");
        when(giftCertificateRepository.findByName("name1")).thenReturn(Optional.empty());
        GiftCertificate giftCertificate = new GiftCertificate("name1");
        when(giftCertificateMapper.dtoToEntity(giftCertificateDto)).thenReturn(giftCertificate);
        when(giftCertificateRepository.saveAndFlush(giftCertificate)).thenReturn(GIFT_CERTIFICATE);
        when(giftCertificateMapper.entityToDto(GIFT_CERTIFICATE)).thenReturn(GIFT_CERTIFICATE_DTO);
        GiftCertificateDto actual = giftCertificateService.save(giftCertificateDto);
        GiftCertificateDto expected =
                new GiftCertificateDto(1, "name1", "description1", 1, 1, null, null, new ArrayList<>());
        Assertions.assertEquals(expected, actual);
        verify(tagRepository, times(0)).findByName(anyString());
        verify(tagRepository, times(0)).save(any());
    }

    @Test
    public void testUpdateShouldThrowExceptionWhenCertificateIdNotFound() {
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(CertificateNotFoundException.class,
                () -> giftCertificateService.update(GIFT_CERTIFICATE_DTO));
    }

    @Test
    public void testUpdateShouldThrowExceptionWhenCertificateNameAlreadyExists() {
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(GIFT_CERTIFICATE));
        when(giftCertificateRepository.findByName("name1")).thenReturn(Optional.of(new GiftCertificate()));
        Assertions.assertThrows(CertificateNameAlreadyExistsException.class,
                () -> giftCertificateService.update(GIFT_CERTIFICATE_DTO));
    }

    @Test
    public void testRemoveShouldThrowExceptionWhenCertificateIdNotFound() {
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(CertificateNotFoundException.class,
                () -> giftCertificateService.remove(1));
    }

    @Test
    public void testUpdatePriceShouldThrowExceptionWhenCertificateNotFound() {
        int idCertificate = 1;
        when(giftCertificateRepository.findById(idCertificate)).thenReturn(Optional.empty());
        Assertions.assertThrows(CertificateNotFoundException.class,
                () -> giftCertificateService.updatePrice(idCertificate, 0));
    }

    @Test
    public void testUpdatePrice() {
        int idCertificate = 1;
        int price = 100;
        GiftCertificate giftCertificate =
                new GiftCertificate(1, "name1", "description1", 0, 0, null, null, false, new ArrayList<>());
        when(giftCertificateRepository.findById(idCertificate)).thenReturn(Optional.of(giftCertificate));
        GiftCertificateDto giftCertificateDto =
                new GiftCertificateDto(1, "name1", "description1", 100, 0, null, null, new ArrayList<>());
        when(giftCertificateMapper.entityToDto(giftCertificate)).thenReturn(giftCertificateDto);
        GiftCertificateDto actual = giftCertificateService.updatePrice(idCertificate, price);
        GiftCertificateDto expected =
                new GiftCertificateDto(1, "name1", "description1", 100, 0, null, null, new ArrayList<>());
        Assertions.assertEquals(expected, actual);
    }

}