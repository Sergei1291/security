package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.certificate.CertificateIsDeletedException;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.exception.order.OrderByUserNotFoundException;
import com.epam.esm.exception.order.OrderNotFoundException;
import com.epam.esm.exception.user.UserNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.model.GiftCertificateDto;
import com.epam.esm.model.OrderDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final GiftCertificateRepository giftCertificateRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderMapper orderMapper,
                            GiftCertificateRepository giftCertificateRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.giftCertificateRepository = giftCertificateRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<OrderDto> findALl(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(orderMapper::entityToDto);
    }

    @Override
    public OrderDto findById(int id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        Order order = optionalOrder.orElseThrow(() -> new OrderNotFoundException("" + id));
        return orderMapper.entityToDto(order);
    }

    @Override
    public OrderDto findOrderByUser(int userId, int orderId) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUser(orderId, userId);
        Order order = optionalOrder.orElseThrow(
                () -> new OrderByUserNotFoundException(userId, orderId));
        return orderMapper.entityToDto(order);
    }

    @Override
    public Page<OrderDto> findAllOrdersByUser(int userId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);
        return orderPage.map(orderMapper::entityToDto);
    }

    @Override
    @Transactional
    public OrderDto makeOrderCertificate(int userId, GiftCertificateDto certificate) {
        int certificateId = certificate.getId();
        Optional<GiftCertificate> optionalGiftCertificate =
                giftCertificateRepository.findById(certificateId);
        GiftCertificate giftCertificate = optionalGiftCertificate.orElseThrow(
                () -> new CertificateNotFoundException("" + certificateId));
        boolean isDeleted = giftCertificate.getIsDeleted();
        if (isDeleted) {
            throw new CertificateIsDeletedException("" + certificateId);
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(
                () -> new UserNotFoundException("" + userId));
        Order createdOrder = createOrder(user, giftCertificate);
        Order savedOrder = orderRepository.saveAndFlush(createdOrder);
        return orderMapper.entityToDto(savedOrder);
    }

    private Order createOrder(User user, GiftCertificate giftCertificate) {
        Order order = new Order();
        int cost = giftCertificate.getPrice();
        order.setCost(cost);
        order.setGiftCertificate(giftCertificate);
        order.setUser(user);
        return order;
    }

}