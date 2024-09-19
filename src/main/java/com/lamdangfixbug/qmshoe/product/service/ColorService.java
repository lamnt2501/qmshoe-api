package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.Color;

import java.util.List;

public interface ColorService {
    Color createColor(Color color);
    Color findColorById(int id);
    List<Color> findAllColors();
    Color updateColor(Color color);
}
