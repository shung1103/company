package org.hanghae99.company.product.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.product.dto.ProductRequestDto;
import org.hanghae99.company.product.dto.ProductResponseDto;
import org.hanghae99.company.product.service.ProductService;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequestDto));
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<ProductResponseDto> getProductInfo(@PathVariable Long productId, @PageableDefault(size = 10) Pageable pageable, @RequestParam(required = false) Long cursor) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductInfo(productId, pageable, cursor));
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ApiResponseDto> deleteProduct(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(productId));
    }
}
