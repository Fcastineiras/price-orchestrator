package com.challenge.priceSelector.integrationTest.price;

import com.challenge.priceSelector.dto.request.PriceToApplyReq;
import com.challenge.priceSelector.dto.response.PriceToApplyRes;
import com.challenge.priceSelector.integrationTest.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
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


public class PriceControllerIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    private void setUp() throws IOException {
        try {
            jdbcTemplate.execute(read("scripts/insert_test_data.sql"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
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
}
