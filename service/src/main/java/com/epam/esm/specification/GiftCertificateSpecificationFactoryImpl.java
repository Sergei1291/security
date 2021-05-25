package com.epam.esm.specification;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.certificate.UnsupportedSearchParamNameCertificateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateSpecificationFactoryImpl implements GiftCertificateSpecificationFactory {

    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";

    private final GiftCertificateSpecification giftCertificateSpecification;

    @Autowired
    public GiftCertificateSpecificationFactoryImpl(GiftCertificateSpecification giftCertificateSpecification) {
        this.giftCertificateSpecification = giftCertificateSpecification;
    }

    @Override
    public Specification<GiftCertificate> create(String searchParamName, String searchString) {
        if (searchParamName == null) {
            searchParamName = NAME;
        }
        switch (searchParamName) {
            case NAME:
                return giftCertificateSpecification.nameLike(searchString);
            case DESCRIPTION:
                return giftCertificateSpecification.descriptionLike(searchString);
            default:
                throw new UnsupportedSearchParamNameCertificateException(searchParamName);
        }
    }

}