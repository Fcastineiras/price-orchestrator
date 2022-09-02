package com.challenge.priceSelector.repository.rowMapper;

import com.challenge.priceSelector.model.Currency;
import com.challenge.priceSelector.model.Price;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.challenge.priceSelector.Utils.AdapterUtils.stringToTime;

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
                stringToTime(rs.getString("start_date")),
                stringToTime(rs.getString("end_date"))
        );
    }
}