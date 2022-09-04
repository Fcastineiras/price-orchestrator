package com.challenge.priceSelector.Utils;

import static com.challenge.priceSelector.Utils.Constants.DATE_TIME_PATTERN;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AdapterUtils {

  public static ObjectMapper JSON_MAPPER =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

  public static LocalDateTime stringToTime(String date) {
    return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
  }

  public static String objectToJson(Object obj) {
    try {
      return JSON_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
