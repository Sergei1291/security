package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Objects;

public class OrderDto extends RepresentationModel<OrderDto> implements Serializable {

    private int id;
    @PositiveOrZero
    private int cost;
    private String purchaseTime;
    @JsonIgnore
    private GiftCertificateDto giftCertificate;
    @JsonIgnore
    private UserDto user;

    public OrderDto() {
    }

    public OrderDto(int cost, String purchaseTime, GiftCertificateDto giftCertificate, UserDto user) {
        this.cost = cost;
        this.purchaseTime = purchaseTime;
        this.giftCertificate = giftCertificate;
        this.user = user;
    }

    public OrderDto(int id, int cost, String purchaseTime, GiftCertificateDto giftCertificate, UserDto user) {
        this.id = id;
        this.cost = cost;
        this.purchaseTime = purchaseTime;
        this.giftCertificate = giftCertificate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public GiftCertificateDto getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificateDto giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        OrderDto order = (OrderDto) o;
        return id == order.id
                && cost == order.cost
                && Objects.equals(purchaseTime, order.purchaseTime)
                && Objects.equals(giftCertificate, order.giftCertificate)
                && Objects.equals(user, order.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                id, cost, purchaseTime, giftCertificate, user);
    }

}