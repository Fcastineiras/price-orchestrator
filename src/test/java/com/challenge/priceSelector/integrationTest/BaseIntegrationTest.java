package com.challenge.priceSelector.integrationTest;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIntegrationTest {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected Flyway flyway;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @AfterEach
    protected void clean() {
        try {
            flyway.clean();
            flyway.migrate();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
