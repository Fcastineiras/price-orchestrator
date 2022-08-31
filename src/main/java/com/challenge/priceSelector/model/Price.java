package com.challenge.priceSelector.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Price {

    public Price() {}

    public Price(Integer brandId, Long productId, BigDecimal price, Integer priority, Currency currency, LocalDateTime startedDate, LocalDateTime endDate) {
        this.brandId = brandId;
        this.productId = productId;
        this.price = price;
        this.priority = priority;
        this.currency = currency;
        this.startedDate = startedDate;
        this.endDate = endDate;
    }

    private Integer brandId;
    private Long productId;
    private BigDecimal price;
    private Integer priority;
    private Currency currency;
    private LocalDateTime startedDate;
    private LocalDateTime endDate;

    public Integer getBrandId() {
        return brandId;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getPriority() {
        return priority;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
