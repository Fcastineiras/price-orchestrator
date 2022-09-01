package com.challenge.priceSelector.dto.response;

import com.challenge.priceSelector.model.Price;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceToApplyRes {

    public PriceToApplyRes (Price price) {
        this.brandId = price.getBrandId();
        this.productId = price.getProductId();
        this.priceList = price.getPriceList();
        this.price = price.getPrice();
        this.startDate = price.getStartDate();
        this.endDate = price.getEndDate();

    }
    private Long productId;
    private Integer brandId;
    private Long priceList;
    private BigDecimal price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Long getProductId() {
        return productId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public Long getPriceList() {
        return priceList;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
