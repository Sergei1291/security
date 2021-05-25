package com.epam.esm.exception.order;

public class OrderByUserNotFoundException extends RuntimeException {

    private int userId;
    private int orderId;

    public OrderByUserNotFoundException(int userId, int orderId) {
        super();
        this.userId = userId;
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public int getOrderId() {
        return orderId;
    }

}