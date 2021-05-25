package com.epam.esm.repository;

import com.epam.esm.config.TestRepositoryConfig;
import com.epam.esm.entity.GiftCertificate;
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
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfig.class)
public class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    private void initDatabase() {
        GiftCertificate giftCertificateOne = new GiftCertificate("one");
        entityManager.persist(giftCertificateOne);
        GiftCertificate giftCertificateTwo = new GiftCertificate("two");
        entityManager.persist(giftCertificateTwo);
    }

    @Test
    @DirtiesContext
    public void testFindByNameShouldReturnOptionalNotEmptyWhenDBContainCertificateName() {
        Optional<GiftCertificate> actual = giftCertificateRepository.findByName("two");
        Assertions.assertTrue(actual.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindByNameShouldReturnOptionalEmptyWhenDBNotContainCertificateName() {
        Optional<GiftCertificate> actual = giftCertificateRepository.findByName("name");
        Assertions.assertFalse(actual.isPresent());
    }

}