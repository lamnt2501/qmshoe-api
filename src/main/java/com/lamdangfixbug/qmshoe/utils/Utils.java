package com.lamdangfixbug.qmshoe.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {
    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    public static String getSlug(String str) {
        return deAccent(str).replaceAll(" ", "-").toLowerCase();
    }
}
