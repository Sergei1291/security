package com.epam.esm.repository;

import com.epam.esm.config.TestRepositoryConfig;
import com.epam.esm.entity.*;
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
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    private void initDatabase() {
        Tag tagOne = new Tag("one");
        entityManager.persist(tagOne);
        Tag tagTwo = new Tag("two");
        entityManager.persist(tagTwo);

        GiftCertificate giftCertificateOne = new GiftCertificate("one");
        giftCertificateOne.setTags(Collections.singletonList(tagOne));
        entityManager.persist(giftCertificateOne);
        GiftCertificate giftCertificateTwo = new GiftCertificate("two");
        giftCertificateOne.setTags(Collections.singletonList(tagTwo));
        entityManager.persist(giftCertificateTwo);

        User user = new User("login", "pass", "name", "surname",
                Collections.singletonList(Role.USER));
        entityManager.persist(user);

        Order orderOne = new Order(0, "", giftCertificateOne, user);
        entityManager.persist(orderOne);
        Order orderTwo = new Order(0, "", giftCertificateTwo, user);
        entityManager.persist(orderTwo);
        Order orderThree = new Order(0, "", giftCertificateTwo, user);
        entityManager.persist(orderThree);
    }

    @Test
    @DirtiesContext
    public void testFindByNameShouldReturnOptionalNotEmptyWhenDBContainTagName() {
        Optional<Tag> actual = tagRepository.findByName("two");
        Assertions.assertTrue(actual.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindByNameShouldReturnOptionalEmptyWhenDBNotContainTagName() {
        Optional<Tag> actual = tagRepository.findByName("name");
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindMostWidelyUsedTagByUserShouldReturnTag() {
        Tag actual = tagRepository.findMostWidelyUsedTagByUser(1);
        Assertions.assertEquals(new Tag(2, "two", null), actual);
    }

}