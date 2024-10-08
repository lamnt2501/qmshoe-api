package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Color;
import com.lamdangfixbug.qmshoe.product.repository.ColorRepository;
import com.lamdangfixbug.qmshoe.product.service.ColorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    public ColorServiceImpl(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public Color createColor(Color color) {
        return colorRepository.save(color);
    }

    @Override
    public List<Color> findAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public Color findColorById(int id) {
        return colorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Couldn't find color with id: " + id));
    }

    @Override
    public Color updateColor(Color color) {
        Color oldColor = findColorById(color.getId());
        String name = color.getName();
        if(name != null && !name.isBlank())oldColor.setName(color.getName());
        String slug = color.getSlug();
        if(slug != null && !slug.isBlank()) oldColor.setSlug(slug);
        String hex = color.getHex();
        if(hex != null && !hex.isBlank()) oldColor.setHex(hex);
        return colorRepository.save(oldColor);

    }
}
