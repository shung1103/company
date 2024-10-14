package org.hanghae99.company.notification.product_user_notification.service;

import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.notification.product_user_notification.entity.ProductUserNotificationHistory;
import org.hanghae99.company.notification.product_user_notification.repository.ProductUserNotificationHistoryRepository;
import org.hanghae99.company.notification.product_user_notification.repository.ProductUserNotificationRepository;
import org.hanghae99.company.product.entity.Product;
import org.hanghae99.company.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductUserNotificationService {
    public ApiResponseDto sendReStockNotificationAdmin(Long productId) {
        return new ApiResponseDto("", HttpStatus.OK.value());
    }
}
