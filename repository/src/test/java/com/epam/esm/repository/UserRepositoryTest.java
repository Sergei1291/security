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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    private void initDatabase() {
        GiftCertificate giftCertificateOne = new GiftCertificate("one");
        entityManager.persist(giftCertificateOne);

        User user = new User("login", "pass", "name", "surname",
                Collections.singletonList(Role.USER));
        entityManager.persist(user);
        User userSecond = new User("loginFirst", "pass", "name", "surname",
                Collections.singletonList(Role.USER));
        entityManager.persist(userSecond);

        Order orderOne = new Order(10, "", giftCertificateOne, userSecond);
        entityManager.persist(orderOne);
        Order orderTwo = new Order(10, "", giftCertificateOne, user);
        entityManager.persist(orderTwo);
        Order orderThree = new Order(10, "", giftCertificateOne, userSecond);
        entityManager.persist(orderThree);
    }

    @Test
    @DirtiesContext
    public void testFindByLoginShouldReturnOptionalNotEmptyWhenDBContainUserLogin() {
        Optional<User> actual = userRepository.findByLogin("loginFirst");
        Assertions.assertTrue(actual.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindByLoginShouldReturnOptionalEmptyWhenDBNotContainUserLogin() {
        Optional<User> actual = userRepository.findByLogin("loginUnknown");
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindUserMaxOrdersSumShouldReturnUserHavingMaxSumOrderCost() {
        User actual = userRepository.findUserMaxOrdersSum();
        Assertions.assertEquals(new User(2, "loginFirst", "pass", "name", "surname",
                Collections.singletonList(Role.USER)), actual);
    }

}