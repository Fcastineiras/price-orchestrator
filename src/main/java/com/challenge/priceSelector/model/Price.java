package com.challenge.priceSelector.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Price {

  public Price(
      Integer brandId,
      Long productId,
      Long priceList,
      BigDecimal price,
      Integer priority,
      Currency currency,
      LocalDateTime startDate,
      LocalDateTime endDate) {
    this.brandId = brandId;
    this.productId = productId;
    this.priceList = priceList;
    this.price = price;
    this.priority = priority;
    this.currency = currency;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  private Integer brandId;
  private Long productId;
  private Long priceList;
  private BigDecimal price;
  private Integer priority;
  private Currency currency;
  private LocalDateTime startDate;
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

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public Long getPriceList() {
    return priceList;
  }
}
