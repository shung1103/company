package org.hanghae99.company.notification.product_notification.service;

import io.github.bucket4j.Bucket;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.notification.product_notification.entity.ProductNotificationHistory;
import org.hanghae99.company.notification.product_notification.repository.ProductNotificationHistoryRepository;
import org.hanghae99.company.notification.product_user_notification.entity.ProductUserNotification;
import org.hanghae99.company.notification.product_user_notification.entity.ProductUserNotificationHistory;
import org.hanghae99.company.notification.product_user_notification.repository.ProductUserNotificationHistoryRepository;
import org.hanghae99.company.notification.product_user_notification.repository.ProductUserNotificationRepository;
import org.hanghae99.company.product.entity.Product;
import org.hanghae99.company.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
@RequiredArgsConstructor
public class ProductNotificationService {
    private final ProductRepository productRepository;
    private final ProductNotificationHistoryRepository productNotificationHistoryRepository;
    private final ProductUserNotificationRepository productUserNotificationRepository;
    private final ProductUserNotificationHistoryRepository notificationHistoryRepository;
    private final Bucket bucket;

    @Transactional
    public ApiResponseDto sendReStockNotification(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));
        long restockCountNow = product.getRestockCount() + 1;
        product.updateReStockCount(restockCountNow);
        Queue<ProductUserNotification> userNotificationList = productUserNotificationRepository.findAllByProductId(productId);

        while (product.getStockStatus() && !userNotificationList.isEmpty()) {
            if (!bucket.tryConsume(1)) continue;

            ProductUserNotification curUser = userNotificationList.poll();
            ProductNotificationHistory productNotificationHistory = new ProductNotificationHistory(curUser.getId(), productId, restockCountNow);
            productNotificationHistoryRepository.save(productNotificationHistory);
            productUserNotificationRepository.delete(curUser);
        }

        ProductUserNotificationHistory productUserNotificationHistory;

        if (notificationHistoryRepository.existsByProductId(productId)) productUserNotificationHistory = notificationHistoryRepository.findByProductId(productId);
        else productUserNotificationHistory = new ProductUserNotificationHistory(productId, "");

        if (product.getStockStatus() && productUserNotificationRepository.existsByProductId(productId)) productUserNotificationHistory.updateNotificationStatus("IN_PROGRESS");
        else if (!product.getStockStatus() && productUserNotificationRepository.existsByProductId(productId)) productUserNotificationHistory.updateNotificationStatus("CANCELED_BY_SOLD_OUT");
        else if (product.getStockStatus() && !productUserNotificationRepository.existsByProductId(productId)) productUserNotificationHistory.updateNotificationStatus("COMPLETED");
        else productUserNotificationHistory.updateNotificationStatus("CANCELED_BY_ERROR");

        notificationHistoryRepository.save(productUserNotificationHistory);

        return new ApiResponseDto("알림이 발송 되었습니다", HttpStatus.OK.value());
    }
}
