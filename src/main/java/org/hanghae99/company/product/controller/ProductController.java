package org.hanghae99.company.product.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.product.dto.ProductRequestDto;
import org.hanghae99.company.product.dto.ProductResponseDto;
import org.hanghae99.company.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    @GetMapping("/products/{productId}/reviews")
    public ProductResponseDto getProductInfo(@PathVariable Long productId) {
        return productService.getProductInfo(productId);
    }

    @DeleteMapping("/product/{productId}")
    public ApiResponseDto deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }
}
