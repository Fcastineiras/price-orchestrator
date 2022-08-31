package com.challenge.priceSelector.controller;

import com.challenge.priceSelector.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PriceController {

    @Autowired
    private PriceService priceService;

    public String getPriceByDate() {
        return priceService.getPriceByDate();
    }
}
