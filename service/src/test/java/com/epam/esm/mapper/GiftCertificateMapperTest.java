package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.model.TagDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = GiftCertificateMapper.class)
public class GiftCertificateMapperTest {

    private final static Tag TAG_ONE = new Tag("one");
    private final static Tag TAG_TWO = new Tag("two");
    private final static List<Tag> TAG_LIST = Arrays.asList(TAG_ONE, TAG_TWO);
    private final static GiftCertificate GIFT_CERTIFICATE =
            new GiftCertificate(1, "name", "", 1, 22, "", "", false, TAG_LIST);

    private final static TagDto TAG_DTO_ONE = new TagDto("one");
    private final static TagDto TAG_DTO_TWO = new TagDto("two");
    private final static List<TagDto> TAG_DTO_LIST = Arrays.asList(TAG_DTO_ONE, TAG_DTO_TWO);
    private final static GiftCertificateDto GIFT_CERTIFICATE_DTO =
            new GiftCertificateDto(1, "name", "", 1, 22, "", "", TAG_DTO_LIST);

    private final GiftCertificateMapper giftCertificateMapper =
            Mappers.getMapper(GiftCertificateMapper.class);

    @Test
    public void testDtoToEntityShouldTransformDtoToEntity() {
        GiftCertificate actual = giftCertificateMapper.dtoToEntity(GIFT_CERTIFICATE_DTO);
        Assertions.assertEquals(GIFT_CERTIFICATE, actual);
    }

    @Test
    public void testEntityToDtoShouldTransformEntityToDto() {
        GiftCertificateDto actual = giftCertificateMapper.entityToDto(GIFT_CERTIFICATE);
        Assertions.assertEquals(GIFT_CERTIFICATE_DTO, actual);
    }

}