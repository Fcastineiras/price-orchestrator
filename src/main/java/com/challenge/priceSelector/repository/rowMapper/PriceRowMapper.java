package com.challenge.priceSelector.repository.rowMapper;

import com.challenge.priceSelector.model.Currency;
import com.challenge.priceSelector.model.Price;
import org.springframework.jdbc.core.RowMapper;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PriceRowMapper implements RowMapper<Price> {

    @Override
    public Price mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Price(
                rs.getInt("brand_id"),
                rs.getLong("product_id"),
                rs.getLong("price_list"),
                rs.getBigDecimal("price"),
                rs.getInt("priority"),
                Currency.valueOf(rs.getString("curr")),
                LocalDateTime.parse(rs.getString("start_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(rs.getString("end_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}