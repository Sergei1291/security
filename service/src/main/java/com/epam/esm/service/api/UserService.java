package com.epam.esm.service.api;

import com.epam.esm.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface define methods for business logic on User.
 *
 * @author Siarhei Katuzhenets
 * @since 13-06-2021
 */
public interface UserService {

    /**
     * This method is used to find all user.
     *
     * @param pageable This information for pagination of users.
     * @return Page contains all founded tags by pageable information.
     */
    Page<UserDto> findALl(Pageable pageable);

    /**
     * This method is used to find user by id.
     *
     * @param id This is id for finding user.
     * @return This is founded by id user.
     */
    UserDto findById(int id);

    /**
     * This method is used to save user and then return saved user.
     *
     * @param userDto This user will be saved.
     * @return This user will be returned.
     */
    UserDto save(UserDto userDto);

}