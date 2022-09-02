package com.challenge.priceSelector.dto.response;

import com.challenge.priceSelector.model.Price;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.challenge.priceSelector.Utils.Constants.DATE_TIME_PATTERN;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceToApplyRes {

    public PriceToApplyRes() {}

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
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime startDate;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
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
