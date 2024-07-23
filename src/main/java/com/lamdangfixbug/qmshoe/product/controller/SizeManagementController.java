package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Size;
import com.lamdangfixbug.qmshoe.product.service.SizeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/management/sizes")
public class SizeManagementController {
    private SizeService sizeService;

    public SizeManagementController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @PostMapping
    public ResponseEntity<Size> createSize(@RequestBody Size size) {
        return new ResponseEntity<>(sizeService.createSize(size), HttpStatus.CREATED);
    }

}
