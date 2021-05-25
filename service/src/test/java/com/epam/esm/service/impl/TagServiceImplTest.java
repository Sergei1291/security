package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.tag.TagNameAlreadyExistsException;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.TagDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class TagServiceImplTest {

    private final static Tag TAG_ZERO = new Tag(0, "oneTag", null);
    private final static Tag TAG_ONE = new Tag(1, "oneTag", null);

    private final static TagDto TAG_DTO_ZERO = new TagDto(0, "oneTag");
    private final static TagDto TAG_DTO_ONE = new TagDto(1, "oneTag");

    private final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    private final TagMapper tagMapper = Mockito.mock(TagMapper.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final TagServiceImpl tagService =
            new TagServiceImpl(tagRepository, tagMapper, userRepository);

    @Test
    public void testFindByIdShouldReturnTag() {
        when(tagRepository.findById(1)).thenReturn(Optional.of(TAG_ONE));
        when(tagMapper.entityToDto(TAG_ONE)).thenReturn(TAG_DTO_ONE);
        TagDto actual = tagService.findById(1);
        Assertions.assertEquals(new TagDto(1, "oneTag"), actual);
    }

    @Test
    public void testFindByIdShouldThrowTagNotFoundExceptionWhenDatabaseNotContainTagId() {
        when(tagRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(TagNotFoundException.class,
                () -> tagService.findById(1));
    }

    @Test
    public void testSaveShouldSaveTagToDatabaseAndReturnSavedTag() {
        when(tagRepository.findByName("oneTag")).thenReturn(Optional.empty());
        when(tagMapper.dtoToEntity(TAG_DTO_ZERO)).thenReturn(TAG_ZERO);
        when(tagRepository.saveAndFlush(TAG_ZERO)).thenReturn(TAG_ONE);
        when(tagMapper.entityToDto(TAG_ONE)).thenReturn(TAG_DTO_ONE);
        TagDto actual = tagService.save(TAG_DTO_ZERO);
        Assertions.assertEquals(new TagDto(1, "oneTag"), actual);
    }

    @Test
    public void testSaveShouldThrowTagNameAlreadyExistsExceptionWhenDatabaseContainTagName() {
        when(tagRepository.findByName("oneTag")).thenReturn(Optional.of(new Tag()));
        Assertions.assertThrows(TagNameAlreadyExistsException.class,
                () -> tagService.save(TAG_DTO_ZERO));
        verify(tagRepository, times(0)).save(any());
    }

    @Test
    public void testRemoveShouldRemoveTagWhenTagFoundedInDataBase() {
        when(tagRepository.findById(1)).thenReturn(Optional.of(new Tag()));
        tagService.remove(1);
        verify(tagRepository, times(1)).delete(any());
    }

    @Test
    public void testRemoveShouldThrowTagNotFoundExceptionWhenDatabaseNotContainTagId() {
        when(tagRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(TagNotFoundException.class,
                () -> tagService.remove(1));
        verify(tagRepository, times(0)).delete(any());
    }

    @Test
    public void testFindMostWidelyUsedTagUserMaxOrderSumShouldReturnTag() {
        when(userRepository.findUserMaxOrdersSum()).thenReturn(new User());
        when(tagRepository.findMostWidelyUsedTagByUser(0)).thenReturn(TAG_ZERO);
        when(tagMapper.entityToDto(TAG_ZERO)).thenReturn(TAG_DTO_ZERO);
        TagDto actual = tagService.findMostWidelyUsedTagUserMaxOrderSum();
        Assertions.assertEquals(new TagDto(0, "oneTag"), actual);
    }

}