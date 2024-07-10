package com.lamdangfixbug.qmshoe.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileUploadService {
    Map uploadImage(MultipartFile file);
}
