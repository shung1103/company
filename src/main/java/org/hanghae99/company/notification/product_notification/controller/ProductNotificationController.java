package org.hanghae99.company.notification.product_notification.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.notification.product_notification.service.ProductNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ProductNotificationController {
    private final ProductNotificationService productNotificationService;

    @PostMapping("/products/{productId}/notifications/re-stock")
    public ResponseEntity<ApiResponseDto> sendReStockNotification(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productNotificationService.sendReStockNotification(productId));
    }
}
