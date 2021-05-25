package com.epam.esm.mapper;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.TagDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(classes = TagMapper.class)
public class TagMapperTest {

    private final static Tag TAG = new Tag(1, "one", new ArrayList<>());
    private final static TagDto TAG_DTO = new TagDto(1, "one");

    private final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    @Test
    public void testDtoToEntityShouldTransformDtoToEntity() {
        Tag actual = tagMapper.dtoToEntity(TAG_DTO);
        Assertions.assertEquals(TAG, actual);
    }

    @Test
    public void testEntityToDtoShouldTransformEntityToDto() {
        TagDto actual = tagMapper.entityToDto(TAG);
        Assertions.assertEquals(TAG_DTO, actual);
    }

}