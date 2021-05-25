package com.epam.esm.service.api;

import com.epam.esm.model.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface define methods for business logic on Tag.
 *
 * @author Siarhei Katuzhenets
 * @since 13-06-2021
 */
public interface TagService {

    /**
     * This method is used to find all tags.
     *
     * @param pageable This is information for pagination for all certificates.
     * @return Page contains all founded tags by pageable information.
     */
    Page<TagDto> findALl(Pageable pageable);

    /**
     * This method is used to find tag by id.
     *
     * @param id This is id for finding tag.
     * @return This is founded by id tag.
     */
    TagDto findById(int id);

    /**
     * This method is used to save tag and then return saved tag.
     *
     * @param tagDto This tag will be saved.
     * @return This tag will be returned with id.
     */
    TagDto save(TagDto tagDto);

    /**
     * This method is used to remove object.
     *
     * @param id This is id for deleting object.
     */
    void remove(int id);

    /**
     * This method is used to find most widely used tag user with max sum of
     * orders.
     *
     * @return The most widely used Tag user with max sum of orders.
     */
    TagDto findMostWidelyUsedTagUserMaxOrderSum();

}