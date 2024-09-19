package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Size;
import com.lamdangfixbug.qmshoe.product.repository.SizeRepository;
import com.lamdangfixbug.qmshoe.product.service.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    public SizeServiceImpl(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Override
    public Size createSize(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    public Size getSizeById(int id) {
        return sizeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Couldn't find size with id: " + id));
    }

    @Override
    public Size updateSize(Size size) {
        Size s = sizeRepository.findById(size.getId()).orElseThrow(()->new ResourceNotFoundException("Couldn't find size with id: " + size.getId()));
        s.setSize(size.getSize());
        s.setDescription(size.getDescription());
        return sizeRepository.save(s);
    }
}
