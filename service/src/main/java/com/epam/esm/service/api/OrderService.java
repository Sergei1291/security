package com.epam.esm.service.api;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.model.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface define methods for business logic on Order of
 * the gift certificate.
 *
 * @author Siarhei Katuzhenets
 * @since 13-06-2021
 */
public interface OrderService {

    /**
     * This method is used to find all orders.
     *
     * @param pageable This is information for pagination for all orders.
     * @return Page contains all founded objects by pageable information.
     */
    Page<OrderDto> findALl(Pageable pageable);

    /**
     * This method is used to find order by id.
     *
     * @param id This is id for finding order.
     * @return This is founded by id order.
     */
    OrderDto findById(int id);

    /**
     * This method is used to find Order with orderId and belonged to User with
     * id equal userId.
     *
     * @param userId  This is user's id for finding Order.
     * @param orderId This is order's id.
     * @return Founded Order with userId and orderId.
     */
    OrderDto findOrderByUser(int userId, int orderId);

    /**
     * This method is used to find all orders by user.
     *
     * @param userId   This is user's id for finding orders.
     * @param pageable This is information for pagination for founded orders.
     * @return Page contains all founded orders by pageable information.
     */
    Page<OrderDto> findAllOrdersByUser(int userId, Pageable pageable);

    /**
     * This method is used to make order of certificate by certificateId for
     * user by userId.
     *
     * @param userId      This is user's id for making Order.
     * @param certificate This is certificate for making Order.
     * @return Certificate's Order by userId and certificateId.
     */
    OrderDto makeOrderCertificate(int userId, GiftCertificateDto certificate);

}