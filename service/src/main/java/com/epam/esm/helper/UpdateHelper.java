package com.epam.esm.helper;

/**
 * This interface defines helper methods for updating objects.
 *
 * @param <T> This is type of object which can used by this interface.
 * @author Siarhei Katuzhenets
 * @since 13-06-2021
 */
public interface UpdateHelper<T> {

    /**
     * This method is used to get not nullable fields of updated object
     * and update the same value's fields of existing object.
     *
     * @param updatedObject  This is object which contains values for
     *                       updating.
     * @param existingObject This is object in which need to update fields
     *                       from updatedIdentifiable.
     */
    void update(T updatedObject, T existingObject);

}