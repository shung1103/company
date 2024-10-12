package org.hanghae99.company.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.product.entity.Product;
import org.hanghae99.company.product.entity.ProductNotificationHistory;
import org.hanghae99.company.product.entity.ProductUserNotification;
import org.hanghae99.company.product.entity.ProductUserNotificationHistory;
import org.hanghae99.company.product.repository.ProductNotificationHistoryRepository;
import org.hanghae99.company.product.repository.ProductRepository;
import org.hanghae99.company.product.repository.ProductUserNotificationHistoryRepository;
import org.hanghae99.company.product.repository.ProductUserNotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductNotificationHistoryRepository productNotificationHistoryRepository;
    private final ProductUserNotificationRepository productUserNotificationRepository;
    private final ProductUserNotificationHistoryRepository NotificationHistoryRepository;

    @Transactional
    public ApiResponseDto sendReStockNotification(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));
        long restockCountNow = product.getRestockCount() + 1;
        product.updateReStockCount(restockCountNow);
        Queue<ProductUserNotification> userNotificationList = productUserNotificationRepository.findAllByProductId(productId);

        long quantityNow = product.getQuantity();
        while (quantityNow != 0 && !userNotificationList.isEmpty()) {
            ProductUserNotification curUser = userNotificationList.poll();
            ProductNotificationHistory productNotificationHistory = new ProductNotificationHistory(curUser.getId(), productId, restockCountNow);
            productNotificationHistoryRepository.save(productNotificationHistory);
            productUserNotificationRepository.delete(curUser);

            quantityNow--;
        }

        if (quantityNow != 0) product.updateQuantity(quantityNow);

        return null;
    }

    public ApiResponseDto sendReStockNotificationAdmin(Long productId) {
        String status = "";
        ProductUserNotificationHistory notificationStatus = new ProductUserNotificationHistory(productId, status);
        return null;
    }
}
