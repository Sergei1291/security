package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.exception.order.OrderByUserNotFoundException;
import com.epam.esm.exception.order.OrderNotFoundException;
import com.epam.esm.exception.user.UserNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.model.OrderDto;
import com.epam.esm.model.UserDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.api.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

    private final static User USER = new User(1, "login1", null, "", "", null);
    private final static GiftCertificate GIFT_CERTIFICATE =
            new GiftCertificate(1, "name1", "description1", 1, 1, "createDate1", null, false, null);
    private final static Order ORDER =
            new Order(1, "time1", GIFT_CERTIFICATE, USER);

    private final static UserDto USER_DTO = new UserDto(1, "login1", null, "", "", null);
    private final static GiftCertificateDto GIFT_CERTIFICATE_DTO =
            new GiftCertificateDto(1, "name1", "description1", 1, 1, "createDate1", null, null);
    private final static OrderDto ORDER_DTO =
            new OrderDto(1, "time1", GIFT_CERTIFICATE_DTO, USER_DTO);

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final OrderMapper orderMapper = Mockito.mock(OrderMapper.class);
    private final GiftCertificateRepository giftCertificateRepository =
            Mockito.mock(GiftCertificateRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final OrderService orderService =
            new OrderServiceImpl(orderRepository, orderMapper,
                    giftCertificateRepository, userRepository);

    @Test
    public void testFindByIdShouldReturnOrder() {
        when(orderRepository.findById(0)).thenReturn(Optional.of(ORDER));
        when(orderMapper.entityToDto(ORDER)).thenReturn(ORDER_DTO);
        OrderDto actual = orderService.findById(0);
        Assertions.assertEquals(new OrderDto(0, 1, "time1",
                GIFT_CERTIFICATE_DTO, USER_DTO), actual);
    }

    @Test
    public void testFindByIdShouldThrowOrderNotFoundExceptionWhenDatabaseNotContainOrderId() {
        when(userRepository.findById(0)).thenReturn(Optional.empty());
        Assertions.assertThrows(OrderNotFoundException.class,
                () -> orderService.findById(0));
    }

    @Test
    public void testFindOrderByUserShouldReturnOrderWhenOrderFounded() {
        when(orderRepository.findByIdAndUser(1, 1)).thenReturn(Optional.of(ORDER));
        when(orderMapper.entityToDto(ORDER)).thenReturn(ORDER_DTO);
        OrderDto actual = orderService.findOrderByUser(1, 1);
        Assertions.assertEquals(new OrderDto(0, 1, "time1",
                GIFT_CERTIFICATE_DTO, USER_DTO), actual);
    }

    @Test
    public void testFindOrderByUserShouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findByIdAndUser(1, 1)).thenReturn(Optional.empty());
        Assertions.assertThrows(OrderByUserNotFoundException.class,
                () -> orderService.findOrderByUser(1, 1));
    }

    @Test
    public void testMakeOrderCertificateShouldThrowExceptionWhenCertificateNotFound() {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        int idCertificate = 1;
        giftCertificateDto.setId(idCertificate);
        when(giftCertificateRepository.findById(idCertificate)).thenReturn(Optional.empty());
        Assertions.assertThrows(CertificateNotFoundException.class,
                () -> orderService.makeOrderCertificate(0, giftCertificateDto));
    }

    @Test
    public void testMakeOrderCertificateShouldThrowExceptionWhenUserNotFound() {
        int userId = 1;
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        int idCertificate = 1;
        giftCertificateDto.setId(idCertificate);
        when(giftCertificateRepository.findById(idCertificate)).thenReturn(Optional.of(new GiftCertificate()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> orderService.makeOrderCertificate(userId, giftCertificateDto));
    }

}