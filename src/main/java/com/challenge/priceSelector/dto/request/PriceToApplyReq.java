package com.challenge.priceSelector.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceToApplyReq {


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
