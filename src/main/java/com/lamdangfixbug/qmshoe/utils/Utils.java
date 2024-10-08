package com.lamdangfixbug.qmshoe.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.Normalizer;
import java.util.Map;
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

    public static Pageable buildPageable(Map<String, Object> params) {
        int page = 0;
        int limit = 20;
        String type = (String) params.get("type");
        String sortBy = type != null ? "createdAt" : "created_At";
        Sort.Direction order = Sort.Direction.DESC;
        if (params.containsKey("page")) {
            page = Math.max(Integer.parseInt((String) params.get("page")) - 1, 0);
        }
        if (params.containsKey("limit")) {
            limit = Math.max(Integer.parseInt((String) params.get("limit")), 1);
        }
        if (params.containsKey("sort")) {
            sortBy = (String) params.get("sort");
            order = Sort.Direction.ASC;
        }
        if (params.containsKey("order")) {
            order = Sort.Direction.valueOf((String) params.get("order"));
        }

        return PageRequest.of(page, limit, Sort.by(order, sortBy));
    }
}
