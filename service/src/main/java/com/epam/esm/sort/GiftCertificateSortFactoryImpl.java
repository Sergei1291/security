package com.epam.esm.sort;

import com.epam.esm.exception.certificate.UnsupportedSortedParamNameCertificateException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateSortFactoryImpl implements GiftCertificateSortFactory {

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String CREATE_DATE = "createDate";

    @Override
    public Sort create(boolean isOrderDesc, String sortParamName) {
        if (sortParamName == null) {
            sortParamName = ID;
        }
        Sort.Direction directionSort;
        if (isOrderDesc) {
            directionSort = Sort.Direction.DESC;
        } else {
            directionSort = Sort.Direction.ASC;
        }
        switch (sortParamName) {
            case ID:
                return Sort.by(directionSort, ID);
            case NAME:
                return Sort.by(directionSort, NAME);
            case CREATE_DATE:
                return Sort.by(directionSort, CREATE_DATE);
            default:
                throw new UnsupportedSortedParamNameCertificateException(sortParamName);
        }
    }

}