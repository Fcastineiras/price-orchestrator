package com.challenge.priceSelector.repository;

import static com.challenge.priceSelector.Utils.AdapterUtils.objectToJson;
import static com.challenge.priceSelector.Utils.Reader.read;

import com.challenge.priceSelector.model.Price;
import com.challenge.priceSelector.model.PriceCriteria;
import com.challenge.priceSelector.repository.rowMapper.PriceRowMapper;
import java.sql.Types;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class PriceRepository {

  private final Logger logger = LoggerFactory.getLogger(PriceRepository.class);

  @Autowired public JdbcTemplate jdbcTemplate;

  public Price getPriceToApplyByCriteria(PriceCriteria priceCriteria) {
    try {
      final String query = read("db/scripts/select_product_price_by_id_and_date.sql");
      final Pair<Object[], int[]> params = getParams(priceCriteria);

      return jdbcTemplate.queryForObject(
          query, params.getKey(), params.getValue(), new PriceRowMapper());
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Not found price by brand_id"
              + priceCriteria.getBrandId()
              + ", product id"
              + priceCriteria.getProductId()
              + "and date "
              + objectToJson(priceCriteria.getDate()));
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Pair<Object[], int[]> getParams(PriceCriteria priceCriteria) {
    final Object[] params =
        new Object[] {
          priceCriteria.getBrandId(),
          priceCriteria.getProductId(),
          priceCriteria.getDate(),
          priceCriteria.getDate()
        };

    final int[] typeParam = new int[] {Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.VARCHAR};
    return new Pair(params, typeParam);
  }
}
