package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.model.OrderDto;
import com.epam.esm.model.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = OrderMapper.class)
public class OrderMapperTest {

    private final static GiftCertificate GIFT_CERTIFICATE = new GiftCertificate("name");
    private final static GiftCertificateDto GIFT_CERTIFICATE_DTO = new GiftCertificateDto("name");
    private final static List<Role> ROLES = Arrays.asList(Role.ADMIN, Role.USER);
    private final static User USER = new User(1, "login", "pass", "name", "surn", ROLES);
    private final static UserDto USER_DTO = new UserDto(1, "login", "pass", "name", "surn", ROLES);
    private final static Order ORDER = new Order(1, "", GIFT_CERTIFICATE, USER);
    private final static OrderDto ORDER_DTO = new OrderDto(1, "", GIFT_CERTIFICATE_DTO, USER_DTO);

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    public void testDtoToEntityShouldTransformDtoToEntity() {
        Order actual = orderMapper.dtoToEntity(ORDER_DTO);
        Assertions.assertEquals(ORDER, actual);
    }

    @Test
    public void testEntityToDtoShouldTransformEntityToDto() {
        OrderDto actual = orderMapper.entityToDto(ORDER);
        Assertions.assertEquals(ORDER_DTO, actual);
    }

}