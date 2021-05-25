package com.epam.esm.specification;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiftCertificateSpecification {

    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static String TAGS = "tags";

    public Specification<GiftCertificate> nameLike(String partName) {
        return (root, query, criteriaBuilder) -> {
            if (partName == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get(NAME), "%" + partName + "%");
        };
    }

    public Specification<GiftCertificate> descriptionLike(String partDescription) {
        return (root, query, criteriaBuilder) -> {
            if (partDescription == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get(DESCRIPTION), "%" + partDescription + "%");
        };
    }

    public Specification<GiftCertificate> hasTagNamesAndCondition(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        Specification<GiftCertificate> specification = hasTagName(tagNames.get(0));
        for (String tagName : tagNames) {
            Specification<GiftCertificate> specificationTagName = hasTagName(tagName);
            specification = specification.and(specificationTagName);
        }
        return specification;
    }

    private Specification<GiftCertificate> hasTagName(String tagName) {
        return (root, query, criteriaBuilder) -> {
            if (tagName == null || tagName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join(TAGS).get(NAME), tagName);
        };
    }

}