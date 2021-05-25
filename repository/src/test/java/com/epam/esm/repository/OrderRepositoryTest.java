package com.epam.esm.repository;

import com.epam.esm.config.TestRepositoryConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfig.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    private void initDatabase() {
        GiftCertificate giftCertificateOne = new GiftCertificate("one");
        entityManager.persist(giftCertificateOne);
        User user = new User("login", "pass", "name", "surname",
                Collections.singletonList(Role.USER));
        entityManager.persist(user);

        Order orderOne = new Order(0, "", giftCertificateOne, user);
        entityManager.persist(orderOne);
        Order orderTwo = new Order(0, "", giftCertificateOne, user);
        entityManager.persist(orderTwo);
        Order orderThree = new Order(0, "", giftCertificateOne, user);
        entityManager.persist(orderThree);
    }

    @Test
    @DirtiesContext
    public void testFindByIdAndUserShouldReturnOptionalNotEmptyWhenDBContainOrderByUser() {
        int orderId = 2;
        int userId = 1;
        Optional<Order> actual = orderRepository.findByIdAndUser(orderId, userId);
        Assertions.assertTrue(actual.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindByIdAndUserShouldReturnOptionalEmptyWhenDBNotContainOrderId() {
        int orderId = 4;
        int userId = 1;
        Optional<Order> actual = orderRepository.findByIdAndUser(orderId, userId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindByIdAndUserShouldReturnOptionalEmptyWhenDBNotContainUserOrder() {
        int orderId = 1;
        int userId = 2;
        Optional<Order> actual = orderRepository.findByIdAndUser(orderId, userId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindByUserIdShouldReturnPageWhenUserId() {
        int userId = 1;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> actual = orderRepository.findByUserId(userId, pageable);
        Assertions.assertEquals(3, actual.getTotalElements());
    }

}