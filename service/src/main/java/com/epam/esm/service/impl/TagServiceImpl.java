package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.tag.TagNameAlreadyExistsException;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.TagDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.api.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final UserRepository userRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          TagMapper tagMapper,
                          UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<TagDto> findALl(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);
        return tagPage.map(tagMapper::entityToDto);
    }

    @Override
    public TagDto findById(int id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        Tag tag = optionalTag.orElseThrow(() -> new TagNotFoundException("" + id));
        return tagMapper.entityToDto(tag);
    }

    @Override
    @Transactional
    public TagDto save(TagDto tagDto) {
        String tagName = tagDto.getName();
        Optional<Tag> optionalTag = tagRepository.findByName(tagName);
        if (optionalTag.isPresent()) {
            throw new TagNameAlreadyExistsException(tagName);
        }
        Tag tag = tagMapper.dtoToEntity(tagDto);
        Tag savedTag = tagRepository.saveAndFlush(tag);
        return tagMapper.entityToDto(savedTag);
    }

    @Override
    @Transactional
    public void remove(int id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        Tag tag = optionalTag.orElseThrow(() -> new TagNotFoundException("" + id));
        tagRepository.delete(tag);
    }

    @Override
    @Transactional
    public TagDto findMostWidelyUsedTagUserMaxOrderSum() {
        User user = userRepository.findUserMaxOrdersSum();
        int userId = user.getId();
        Tag tag = tagRepository.findMostWidelyUsedTagByUser(userId);
        return tagMapper.entityToDto(tag);
    }

}