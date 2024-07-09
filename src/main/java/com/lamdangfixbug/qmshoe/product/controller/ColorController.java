package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Color;
import com.lamdangfixbug.qmshoe.product.repository.ColorRepository;
import com.lamdangfixbug.qmshoe.product.service.ColorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colors")
public class ColorController {
    private final ColorService colorService;

    public ColorController(final ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public ResponseEntity<List<Color>> getAllColors() {
        return new ResponseEntity<>(colorService.findAllColors(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Color> createColor(@RequestBody final Color color) {
        return new ResponseEntity<>(colorService.createColor(color), HttpStatus.CREATED);
    }
}
