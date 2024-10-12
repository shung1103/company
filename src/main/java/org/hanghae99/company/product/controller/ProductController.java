package org.hanghae99.company.product.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products/{productId}/notifications/re-stock")
    public ResponseEntity<ApiResponseDto> sendReStockNotification(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.sendReStockNotification(productId));
    }

    @PostMapping("/admin/products/{productId}/notifications/re-stock")
    public ResponseEntity<ApiResponseDto> sendReStockNotificationAdmin(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.sendReStockNotificationAdmin(productId));
    }
}
