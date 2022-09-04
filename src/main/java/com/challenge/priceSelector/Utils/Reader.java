package com.challenge.priceSelector.Utils;

import static com.google.common.io.Resources.getResource;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;

public class Reader {
  public static String read(String path) throws IOException {
    return Resources.toString(getResource(path), Charsets.UTF_8);
  }
}
