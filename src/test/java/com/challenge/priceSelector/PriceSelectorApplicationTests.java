package com.challenge.priceSelector;

import com.challenge.priceSelector.controller.PriceController;
import com.challenge.priceSelector.repository.PriceRepository;
import com.challenge.priceSelector.service.PriceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PriceSelectorApplicationTests {

	@Autowired private PriceController priceController;
	@Autowired private PriceService priceService;
	@Autowired private PriceRepository priceRepository;

	@Test
	void contextLoads() {
		assertNotNull(priceController);
		assertNotNull(priceService);
		assertNotNull(priceRepository);
	}

}
