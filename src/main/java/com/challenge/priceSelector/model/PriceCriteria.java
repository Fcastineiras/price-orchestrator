package com.challenge.priceSelector.model;

import com.challenge.priceSelector.dto.request.PriceToApplyReq;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

public class PriceCriteria {

    public PriceCriteria(PriceToApplyReq criteria) {
        this.date = criteria.getDate();
        this.productId = criteria.getProductId();
        this.brandId = criteria.getBrandId();
    }
    private LocalDateTime date;
    private Long productId;
    private Integer brandId;

    public LocalDateTime getDate() {
        return date;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getBrandId() {
        return brandId;
    }
}
