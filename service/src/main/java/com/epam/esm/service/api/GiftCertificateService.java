package com.epam.esm.service.api;

import com.epam.esm.model.GiftCertificateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This interface define methods for business logic on GiftCertificate.
 *
 * @author Siarhei Katuzhenets
 * @since 13-06-2021
 */
public interface GiftCertificateService {

    /**
     * This method is used to find all objects.
     *
     * @param pageable This is information for pagination for all certificates.
     * @return Page contains all founded objects by pageable information.
     */
    Page<GiftCertificateDto> findALl(Pageable pageable);

    /**
     * This method is used to find certificate by id.
     *
     * @param id This is id for finding certificate.
     * @return This is founded by id certificate.
     */
    GiftCertificateDto findById(int id);

    /**
     * This method is used to save certificate and then return saved certificate.
     *
     * @param giftCertificateDto This certificate will be saved.
     * @return This saved certificate will be returned.
     */
    GiftCertificateDto save(GiftCertificateDto giftCertificateDto);

    /**
     * This method is used to update certificate and then return updated certificate.
     * The certificate is updated by id and updated only not null fields of object.
     *
     * @param giftCertificateDto This certificate contains fields for updating.
     * @return This certificate will be returned after updating.
     */
    GiftCertificateDto update(GiftCertificateDto giftCertificateDto);

    /**
     * This method is used to remove certificate.
     *
     * @param id This is id for deleting certificate.
     */
    void remove(int id);

    /**
     * This method is used to find list of all GiftCertificate objects
     * by several params: tag's names, part of searched param. Also list of
     * objects can be sorted by sortParamName by order equal param orderDesc.
     * Some of params of method can be absent.
     *
     * @param tagNames        These are names of tags for searching.
     * @param searchString    This is search string.
     * @param searchParamName This is name search param.
     * @param sortParamName   This is name param for sorting.
     * @param isOrderDesc     This is direction for sorting.
     * @param page            This is num page for finding objects.
     * @param size            This is max object's quantity for one page.
     * @return Page contains all founded objects for page.
     */
    Page<GiftCertificateDto> search(List<String> tagNames,
                                    String searchString,
                                    String searchParamName,
                                    String sortParamName,
                                    boolean isOrderDesc,
                                    int page,
                                    int size);

    /**
     * This method is used to update only one field of GiftCertificate object
     * is price. Update operation do for GiftCertificate by idCertificate.
     *
     * @param idCertificate This is id for updating GiftCertificate.
     * @param price         This is new price for GiftCertificate.
     * @return Updated GiftCertificate.
     */
    GiftCertificateDto updatePrice(int idCertificate, int price);

}