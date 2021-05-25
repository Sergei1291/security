package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.GiftCertificateDto;
import org.mapstruct.Mapper;

@Mapper(uses = TagMapper.class)
public interface GiftCertificateMapper {

    GiftCertificate dtoToEntity(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto entityToDto(GiftCertificate giftCertificate);

}