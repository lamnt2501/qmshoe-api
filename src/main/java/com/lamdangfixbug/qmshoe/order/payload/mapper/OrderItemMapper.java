package com.lamdangfixbug.qmshoe.order.payload.mapper;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.order.entity.OrderItem;
import com.lamdangfixbug.qmshoe.order.payload.response.OrderItemResponse;
import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import com.lamdangfixbug.qmshoe.product.repository.ProductOptionRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderItemMapper {
    public final ProductOptionRepository productOptionRepository;

    public OrderItemMapper(ProductOptionRepository productOptionRepository) {
        this.productOptionRepository = productOptionRepository;
    }

    public OrderItemResponse map(final OrderItem orderItem) {
        OrderItemResponse.OrderItemResponseBuilder builder = OrderItemResponse.builder();
        ProductOption productOption = productOptionRepository.findBySku(orderItem.getId().getSku()).orElseThrow(() -> new ResourceNotFoundException("Product option not found"));
        builder.sku(orderItem.getId().getSku()).quantity(orderItem.getQuantity())
                .price(productOption.getPrice())
                .size(productOption.getSize().getSize())
                .color(productOption.getColor().getName())
                .name(productOption.getProduct().getName())
                .imgUrl(productOption.getProduct().getProductImages().stream().filter(i-> Objects.equals(i.getColor().getId(), productOption.getColor().getId())).toList().getFirst().getUrl());
        return builder.build();
    }
}
