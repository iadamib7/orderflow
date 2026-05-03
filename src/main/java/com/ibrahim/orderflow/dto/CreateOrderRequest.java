package com.ibrahim.orderflow.dto;

import com.ibrahim.orderflow.model.OrderSide;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// REPRESENTS THE JSON BODY FOR CREATING A NEW ORDER
public class CreateOrderRequest {

    // Require a symbol like AAPL or BTCUSD.
    @NotBlank(message = "Symbol is required.")
    private String symbol;

    // Require BUY or SELL.
    @NotNull(message = "Order side is required.")
    private OrderSide side;

    // Require a positive limit price.
    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0001", message = "Price must be greater than zero.")
    private BigDecimal price;

    // Require a positive quantity.
    @Positive(message = "Quantity must be greater than zero.")
    private long quantity;

    public String getSymbol() {
        return symbol;
    }

    public OrderSide getSide() {
        return side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}