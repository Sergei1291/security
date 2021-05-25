package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByName(String name);

    @Query(value = "select * from tag " +
            "join gift_certificate_tag on gift_certificate_tag.tag = tag.id " +
            "join order_certificate on order_certificate.certificate_id=gift_certificate_tag.certificate " +
            "where order_certificate.user_id = :userId group by tag.id order by count(tag.id) desc limit 1",
            nativeQuery = true)
    Tag findMostWidelyUsedTagByUser(@Param("userId") int userId);

}