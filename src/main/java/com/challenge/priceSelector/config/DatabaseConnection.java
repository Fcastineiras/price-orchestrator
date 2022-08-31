package com.challenge.priceSelector.config;

import com.google.common.collect.Lists;

import java.util.List;

public class DatabaseConnection {

    private static final String TABLES_CREATION_SCRIPTS_PATH = "/db_scripts/db_in_memory/tables_creation/";
    private static final List<String> TABLES_CREATION = Lists.newArrayList(
            "price_creation.sql");
    private static final String INPUT_DATA_SCRIPTS_PATH = "/db_migration/input_data/";
    private static final List<String> INPUT_DATA = Lists.newArrayList(
            "price_data.sql");
}
