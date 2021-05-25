package com.epam.esm.specification;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.domain.Specification;

/**
 * This interface define methods creating Specification object for searching
 * GiftCertificate object.
 *
 * @author Siarhei Katuzhenets
 * @since 13-06-2021
 */
public interface GiftCertificateSpecificationFactory {

    /**
     * This method is used to create Specification object for searching by
     * searchParamName parameter of GiftCertificate object.
     *
     * @param searchParamName This is parameter's name for searching.
     * @param searchString    This is part of searching parameter.
     * @return This is Specification object for searching.
     */
    Specification<GiftCertificate> create(String searchParamName, String searchString);

}