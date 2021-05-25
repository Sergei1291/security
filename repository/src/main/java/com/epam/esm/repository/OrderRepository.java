package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("FROM Order o where o.id = :orderId and o.user.id = :userId")
    Optional<Order> findByIdAndUser(@Param("orderId") int orderId,
                                    @Param("userId") int userId);

    @Query(value = "FROM Order o where o.user.id = :userId")
    Page<Order> findByUserId(@Param("userId") int userId, Pageable pageable);

}