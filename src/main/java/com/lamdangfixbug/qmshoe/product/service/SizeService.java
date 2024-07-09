package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.Size;

import java.util.List;

public interface SizeService {
    Size createSize(Size size);
    Size getSizeById(int id);
    Size updateSize(Size size);
    List<Size> getAllSizes();
}
