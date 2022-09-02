package com.challenge.priceSelector.integrationTest;

import com.challenge.priceSelector.Utils.Reader;
import com.challenge.priceSelector.dto.request.PriceToApplyReq;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static com.challenge.priceSelector.Utils.AdapterUtils.objectToJson;
import static com.challenge.priceSelector.Utils.Reader.read;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PriceControllerIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(PriceControllerIntegrationTest.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void setUp() {
        jdbcTemplate.execute(insertBrandsToTest);
        jdbcTemplate.execute(insertProductsToTest);
    }

    @AfterEach
    private void clean() {
        jdbcTemplate.execute("delete from price");
        jdbcTemplate.execute("delete from brand");
        try {
            jdbcTemplate.execute(read("db/migration/V1__create_band_table.sql"));
            jdbcTemplate.execute(read("db/migration/V2__insert_brand_data.sql"));
            jdbcTemplate.execute(read("db/migration/V3__create_price_table.sql"));
            jdbcTemplate.execute(read("db/migration/V4__insert_price_data.sql"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void whenTryToGetPriceWithValidCriteriaBodyThenReturnPrice() throws Exception {
        final String req = read("jsonTest/price/basic_valid_case.json");

        final MvcResult actualResponse = mvc
                .perform(MockMvcRequestBuilders.get("/price/")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        logger.info(objectToJson(actualResponse.getResponse()));
    }

    @Test
    public void whenTryToGetPriceWithInvalidCriteriaBodyThenThrowBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/price/")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("sdfsg"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private final String insertBrandsToTest = "INSERT INTO public.brand (id, name) VALUES" +
            "(2, 'tests'); ";

    private final String insertProductsToTest = "INSERT INTO public.price (brand_id, price_list, " +
            "product_id, priority, price, curr, start_date, end_date ) VALUES " +
            "(2, 4, 35455, 1, 38.95, 'EUR', '2020-06-15 16:00:00', '2020-12-31 23:59:59')," +
            "(1, 4, 35456, 1, 38.95, 'EUR', '2020-06-15 16:00:00', '2020-12-31 23:59:59');";
}
