package com.epam.esm.mapper;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.TagDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag dtoToEntity(TagDto tagDto);

    TagDto entityToDto(Tag tag);

}