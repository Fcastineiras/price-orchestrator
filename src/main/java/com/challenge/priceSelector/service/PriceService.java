package com.challenge.priceSelector.service;

import com.challenge.priceSelector.dto.request.PriceToApplyReq;
import com.challenge.priceSelector.model.Price;
import com.challenge.priceSelector.model.PriceCriteria;
import com.challenge.priceSelector.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

  @Autowired private PriceRepository priceRepository;

  public Price getPriceToApplyByCriteria(PriceToApplyReq priceToApplyReq) {
    return priceRepository.getPriceToApplyByCriteria(new PriceCriteria(priceToApplyReq));
  }
}
