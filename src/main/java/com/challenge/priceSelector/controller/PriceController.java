package com.challenge.priceSelector.controller;

import com.challenge.priceSelector.dto.request.PriceToApplyReq;
import com.challenge.priceSelector.dto.response.PriceToApplyRes;
import com.challenge.priceSelector.model.Price;
import com.challenge.priceSelector.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/price")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/")
    public ResponseEntity<PriceToApplyRes> getPriceToApplyByCriteria (
           @RequestBody PriceToApplyReq priceToApplyReq
    ) {
        validate(priceToApplyReq);
        final Price price = priceService.getPriceToApplyByCriteria(priceToApplyReq);
        return new ResponseEntity(new PriceToApplyRes(price), HttpStatus.valueOf(200));
    }

    private void validate(PriceToApplyReq req) {
        if (req.getBrandId() == null
        || req.getProductId() == null
        || req.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
