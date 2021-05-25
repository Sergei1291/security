package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    @Query(value = "select * from user " +
            "inner join (select user_id from order_certificate " +
            "group by user_id order by sum(cost) desc limit 1) user_max_order_sum " +
            "on user_max_order_sum.user_id=user.id",
            nativeQuery = true)
    User findUserMaxOrdersSum();

}