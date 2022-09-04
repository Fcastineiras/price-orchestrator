package com.challenge.priceSelector.price;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.challenge.priceSelector.dto.request.PriceToApplyReq;
import com.challenge.priceSelector.model.PriceCriteria;
import com.challenge.priceSelector.repository.PriceRepository;
import com.challenge.priceSelector.repository.rowMapper.PriceRowMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

public class PriceRepositoryTest {

  private final JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
  private final PriceRepository priceRepository = new PriceRepository();

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(priceRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  public void givenJdbcTemplateThrowExceptionWhenGetPriceByCriteriaThenThrowException() {
    when(jdbcTemplate.queryForObject(any(), any(), any(), any(PriceRowMapper.class)))
        .thenThrow(new RuntimeException());
    final PriceCriteria criteria =
        new PriceCriteria(new PriceToApplyReq(1, 1L, LocalDateTime.now()));
    try {
      priceRepository.getPriceToApplyByCriteria(criteria);
    } catch (Exception e) {
      Assertions.assertEquals(ResponseStatusException.class, e.getClass());
    }
  }
}
