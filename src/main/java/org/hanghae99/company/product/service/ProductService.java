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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductNotificationHistoryRepository productNotificationHistoryRepository;
    private final ProductUserNotificationRepository productUserNotificationRepository;
    private final ProductUserNotificationHistoryRepository notificationHistoryRepository;

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

        product.updateQuantity(quantityNow);
        productRepository.save(product);

        return new ApiResponseDto("알림이 발송 되었습니다", HttpStatus.OK.value());
    }

    public ApiResponseDto sendReStockNotificationAdmin(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));

        ProductUserNotificationHistory productUserNotificationHistory;

        if (notificationHistoryRepository.existsByProductId(productId)) productUserNotificationHistory = notificationHistoryRepository.findByProductId(productId);
        else productUserNotificationHistory = new ProductUserNotificationHistory(productId, "");

        if (product.getQuantity() != 0 && productUserNotificationRepository.existsByProductId(productId)) productUserNotificationHistory.updateNotificationStatus("IN_PROGRESS");
        else if (product.getQuantity() == 0 && productUserNotificationRepository.existsByProductId(productId)) productUserNotificationHistory.updateNotificationStatus("CANCELED_BY_SOLD_OUT");
        else if (product.getQuantity() != 0 && !productUserNotificationRepository.existsByProductId(productId)) productUserNotificationHistory.updateNotificationStatus("COMPLETED");
        else productUserNotificationHistory.updateNotificationStatus("CANCELED_BY_ERROR");

        notificationHistoryRepository.save(productUserNotificationHistory);

        return new ApiResponseDto("재입고 알림 전송 상태를 업데이트 하였습니다", HttpStatus.CREATED.value());
    }
}
