package com.challenge.priceSelector.service;

import com.challenge.priceSelector.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public String getPriceByDate() {
        return priceRepository.getPriceByDate();
    }

}
