package com.kamron.pogoiv.utils;

import java.text.Normalizer;

public class StringUtils {
/**
 * Returns the normalized string for simplifying words to detect pokemons.
 */
    public static String normalize(String s) {
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{M}]", "");

        return s;
    }
}
