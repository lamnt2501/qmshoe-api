package com.lamdangfixbug.qmshoe.product.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lamdangfixbug.qmshoe.exceptions.FileUploadException;
import com.lamdangfixbug.qmshoe.product.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private final Cloudinary cloudinary;

    public FileUploadServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is empty!");
        }
        if (!file.getContentType().contains("image")) {
            throw new FileUploadException(file.getOriginalFilename() + " is not an image!");
        }

        byte[] data;
        Map response;
        Map params1 = ObjectUtils.asMap(
                "use_filename", false,
                "unique_filename", false,
                "overwrite", false
        );

        try {
            data = file.getBytes();
            response = cloudinary.uploader().upload(data, params1);
        } catch (IOException e) {
            throw new FileUploadException("Something went wrong when uploading an image!");
        }

        return response;
    }
}
