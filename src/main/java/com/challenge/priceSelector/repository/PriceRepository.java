package com.challenge.priceSelector.repository;

import com.challenge.priceSelector.model.Price;
import com.challenge.priceSelector.model.PriceCriteria;
import com.challenge.priceSelector.repository.rowMapper.PriceRowMapper;
import org.flywaydb.core.internal.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Types;

@Repository
public class PriceRepository {

    private final Logger logger = LoggerFactory.getLogger(PriceRepository.class);

    @Autowired
    public JdbcTemplate jdbcTemplate;

    public Price getPriceToApplyByCriteria(PriceCriteria priceCriteria) {
        try {
            final Path path = Paths.get(this.getClass().getClassLoader().getResource("db/scripts/select_product_price_by_id_and_date.sql").getPath());
            final String query = FileUtils.readAsString(path);


            final Object[] params = new Object[]{priceCriteria.getBrandId(),
                    priceCriteria.getProductId(),
                    priceCriteria.getDate(), priceCriteria.getDate()
            };
            final int[] typeParam = new int[]{Types.INTEGER, Types.BIGINT,
                    Types.VARCHAR, Types.VARCHAR
            };
            final Price found = jdbcTemplate.queryForObject(query, params, typeParam, new PriceRowMapper());
            logger.info("prueba encontrada {}", found.toString());
            return found;
        } catch (EmptyResultDataAccessException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage());
            //throws FileNotFoundException
            return null;
        }
    }

}
