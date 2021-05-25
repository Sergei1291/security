package com.epam.esm.mapper;

import com.epam.esm.entity.Order;
import com.epam.esm.model.OrderDto;
import org.mapstruct.Mapper;

@Mapper(uses = {GiftCertificateMapper.class, UserMapper.class})
public interface OrderMapper {

    Order dtoToEntity(OrderDto orderDto);

    OrderDto entityToDto(Order order);

}