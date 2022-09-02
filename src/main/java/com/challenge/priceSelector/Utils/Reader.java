package com.challenge.priceSelector.Utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.toString;

public class Reader {
    public static String read(String path) throws IOException {
        return Resources.toString(getResource(path), Charsets.UTF_8);
    }
}
