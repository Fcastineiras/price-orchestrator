package com.challenge.priceSelector.integrationTest;

import com.challenge.priceSelector.dto.request.PriceToApplyReq;
import com.challenge.priceSelector.dto.response.PriceToApplyRes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.challenge.priceSelector.Utils.AdapterUtils.*;
import static com.challenge.priceSelector.Utils.Reader.read;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @ParameterizedTest
    @MethodSource("successCases")
    public void GivenExistsRegisterByCriteriaBodyWhenTryToGetPriceThenReturnPrice(
            Integer brandId, Long productId, String date, BigDecimal priceExpected) throws Exception {
        final PriceToApplyReq req = new PriceToApplyReq(brandId, productId, stringToTime(date));


        final PriceToApplyRes res = JSON_MAPPER.readValue(mvc
                .perform(MockMvcRequestBuilders.get("/price/")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(req)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), PriceToApplyRes.class);

        assertEquals(req.getBrandId(), res.getBrandId());
        assertEquals(req.getProductId(), res.getProductId());
        assertEquals(priceExpected, res.getPrice());
        assertTrue(res.getStartDate().isBefore(req.getDate())
                || res.getStartDate().isEqual(req.getDate()));
        assertTrue(res.getEndDate().isAfter(req.getDate())
                || res.getEndDate().isEqual(req.getDate()));
        assertTrue(res.getEndDate().isAfter(req.getDate())
                || res.getEndDate().isEqual(req.getDate()));

    }

    @ParameterizedTest
    @MethodSource("notFoundCases")
    public void GivenNotExistsAnyRegisterByCriteriaBodyWhenTryToGetPriceThenThrowNotFound(
            Integer brandId, Long productId, String date) throws Exception {
        final PriceToApplyReq req = new PriceToApplyReq(brandId, productId, stringToTime(date));

        mvc.perform(MockMvcRequestBuilders.get("/price/")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(req)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("badRequestCases")
    public void whenTryToGetPriceWithInvalidCriteriaBodyThenThrowBadRequest(String body) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/price/")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    public static Stream<Arguments> notFoundCases() {
        return Stream.of(
                Arguments.of(9, 35456L, "2020-06-14 13:00:00"), // not exists brand
                Arguments.of(1, 1111L, "2020-06-15 13:00:00"), // not exists product
                // date cases
                Arguments.of(1, 35455L, "1999-06-14 13:00:00"), // date before than exists
                Arguments.of(1, 35455L, "2050-06-14 13:00:00") // date after than exists
        );
    }

    public static Stream<Arguments> successCases() {
        return Stream.of(

                // distinct brands with same product id
                Arguments.of(2, 35456L, "2020-06-14 13:00:00", new BigDecimal("50.50")),
                Arguments.of(1, 35456L, "2020-06-15 13:00:00", new BigDecimal("30.30")),
                Arguments.of(1, 35457L, "2020-06-15 13:00:00", new BigDecimal("40.40")),

                // date cases
                Arguments.of(3, 222L, "2022-01-15 16:00:00", new BigDecimal("60.60")), // start limit date
                Arguments.of(3, 222L, "2022-01-15 16:00:01", new BigDecimal("60.60")), // at span time
                Arguments.of(3, 222L, "2022-01-15 16:00:02", new BigDecimal("60.60")), // end limit date

                // priority cases
                Arguments.of(4, 123L, "2020-01-02 00:00:00", new BigDecimal("70.70")), // priority 0
                Arguments.of(4, 123L, "2020-01-01 00:00:00", new BigDecimal("80.80")), // priority 1
                Arguments.of(4, 123L, "2020-02-02 23:59:59", new BigDecimal("80.80")), // priority 1
                Arguments.of(4, 123L, "2012-01-01 16:00:00", new BigDecimal("90.90")), // priority 2
                Arguments.of(4, 123L, "2027-01-01 16:00:00", new BigDecimal("90.90")) // priority 2
        );
    }

    public static Stream<Arguments> badRequestCases() {
        return Stream.of(
                Arguments.of("afsafas"),
                Arguments.of("{}"),
                Arguments.of("{\n" +
                        "    \"brand_id\":\"sdfds\",\n" +
                        "    \"product_id\":35455,\n" +
                        "    \"date\":\"2020-06-14 23:59:00\"\n" +
                        "}"),
                Arguments.of("{\n" +
                        "    \"brand_id\":1,\n" +
                        "    \"date\":\"2020-06-14 23:59:00\"\n" +
                        "}"),
                Arguments.of("{\n" +
                        "    \"product_id\":35455,\n" +
                        "    \"date\":\"2020-06-14 23:59:00\"\n" +
                        "}"),
                Arguments.of("{\n" +
                        "    \"brand_id\":1,\n" +
                        "    \"product_id\":35455,\n" +
                        "}"),
                Arguments.of("{\n" +
                        "    \"brand_id\":1,\n" +
                        "    \"product_id\":adfa,\n" +
                        "    \"date\":\"2020-06-14 23:59:00\"\n" +
                        "}"),
                Arguments.of("{\n" +
                        "    \"brand_id\":1,\n" +
                        "    \"product_id\":35455,\n" +
                        "    \"date\":\"afsf\"\n" +
                        "}")
        );
    }

    private final String insertBrandsToTest = "INSERT INTO public.brand (id, name) VALUES" +
            "(2, 'tests brand')," +
            "(3, 'tests brand 3')," +
            "(4, 'tests brand 4'); ";

    private final String insertProductsToTest = "INSERT INTO public.price (brand_id, price_list, " +
            "product_id, priority, price, curr, start_date, end_date ) VALUES " +
            "(1, 4, 35456, 1, 30.30, 'EUR', '2010-06-15 16:00:00', '2030-12-31 23:59:59')," +
            "(1, 4, 35457, 1, 40.40, 'EUR', '2010-06-15 16:00:00', '2030-12-31 23:59:59')," +
            "(2, 1, 35456, 1, 50.50, 'EUR', '2010-06-15 16:00:00', '2030-12-31 23:59:59')," +
            "(3, 1, 222, 0, 60.60, 'EUR', '2022-01-15 16:00:00', '2022-01-15 16:00:02')," +
            "(4, 2, 123, 0, 70.70, 'EUR', '2020-01-02 00:00:00', '2020-02-01 23:59:59')," +
            "(4, 3, 123, 1, 80.80, 'EUR', '2015-01-01 16:00:00', '2025-12-31 23:59:59')," +
            "(4, 3, 123, 2, 90.90, 'EUR', '2010-01-01 16:00:00', '2030-12-31 23:59:59');";
}
