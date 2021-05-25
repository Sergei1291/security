package com.epam.esm.sort;

import org.springframework.data.domain.Sort;

/**
 * This interface define methods creating Sort object for GiftCertificate
 * object.
 *
 * @author Siarhei Katuzhenets
 * @since 13-06-2021
 */
public interface GiftCertificateSortFactory {

    /**
     * This method is used to create Sort object order according to isOrderDesc
     * and sorting will be do by sortParamName parameters.
     *
     * @param isOrderDesc   This is direction for sorting.
     * @param sortParamName This is name of sorting parameter.
     * @return This is Sort object for Pageable object.
     */
    Sort create(boolean isOrderDesc, String sortParamName);

}