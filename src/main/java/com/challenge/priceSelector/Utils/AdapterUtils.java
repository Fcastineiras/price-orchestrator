package com.challenge.priceSelector.Utils;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.challenge.priceSelector.Utils.Constants.DATE_TIME_PATTERN;

public class AdapterUtils {

    public static LocalDateTime stringToTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public static String objectToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
