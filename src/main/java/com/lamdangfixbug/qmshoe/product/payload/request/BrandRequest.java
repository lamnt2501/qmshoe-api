package com.lamdangfixbug.qmshoe.product.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
    private String name;
    private String description;
    private MultipartFile image;
}
