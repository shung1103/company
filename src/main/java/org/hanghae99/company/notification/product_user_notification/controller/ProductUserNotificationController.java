package org.hanghae99.company.notification.product_user_notification.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.notification.product_user_notification.service.ProductUserNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ProductUserNotificationController {
    private final ProductUserNotificationService productUserNotificationService;

    @PostMapping("/admin/products/{productId}/notifications/re-stock")
    public ResponseEntity<ApiResponseDto> sendReStockNotificationAdmin(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productUserNotificationService.sendReStockNotificationAdmin(productId));
    }
}
