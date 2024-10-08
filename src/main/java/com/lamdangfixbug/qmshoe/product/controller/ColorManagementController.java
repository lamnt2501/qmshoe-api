package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Color;
import com.lamdangfixbug.qmshoe.product.service.ColorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management/colors")
public class ColorManagementController {
    private final ColorService colorService;

    public ColorManagementController(final ColorService colorService) {
        this.colorService = colorService;
    }

    @PostMapping
    public ResponseEntity<Color> createColor(@RequestBody final Color color) {
        return new ResponseEntity<>(colorService.createColor(color), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Color> updateColor(@RequestBody final Color color) {
        return new ResponseEntity<>(colorService.updateColor(color), HttpStatus.OK);
    }
}
