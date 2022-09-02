package com.challenge.priceSelector.dto.request;

import com.challenge.priceSelector.Utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

import static com.challenge.priceSelector.Utils.Constants.DATE_TIME_PATTERN;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceToApplyReq {


    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime date;
    private Long productId;
    private Integer brandId;

    public PriceToApplyReq() {
    }

    public PriceToApplyReq(Integer brandId, Long productId, LocalDateTime date) {
        this.brandId = brandId;
        this.productId = productId;
        this.date = date;
    }

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
