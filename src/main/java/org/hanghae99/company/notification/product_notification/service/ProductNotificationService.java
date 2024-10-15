package org.hanghae99.company.notification.product_notification.service;

import io.github.bucket4j.Bucket;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.notification.product_notification.entity.ProductNotificationHistory;
import org.hanghae99.company.notification.product_notification.repository.ProductNotificationHistoryRepository;
import org.hanghae99.company.notification.product_user_notification.controller.ProductUserNotificationController;
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
    private final ProductUserNotificationController productUserNotificationController;
    private final Bucket bucket;

    @Transactional
    public ApiResponseDto sendReStockNotification(Long productId) {
        long lastUserId = productId;
        ProductUserNotificationHistory productUserNotificationHistory;
        // 해당 상품의 재입고 알림 전송 이력과 전송 상태가 DB에 있는지 확인. 없을 경우 생성하여 저장
        if (notificationHistoryRepository.existsByProductId(productId)) {
            productUserNotificationHistory = notificationHistoryRepository.findByProductId(productId);
            productUserNotificationHistory.updateNotificationStatus("IN_PROGRESS");
            notificationHistoryRepository.save(productUserNotificationHistory);
        } else {
            productUserNotificationHistory = new ProductUserNotificationHistory(productId, "IN_PROGRESS");
            notificationHistoryRepository.save(productUserNotificationHistory);
        }

        // 에러에 의한 발송 정지를 확인하기 위해 try-catch 문 사용
        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));
            // 재입고 회차 +1
            long restockCountNow = product.getRestockCount() + 1;
            product.updateReStockCount(restockCountNow);
            // 재입고 알림 신청을 한 사람 순서대로 알림을 보내기 위해 Queue를 사용
            Queue<ProductUserNotification> userNotificationList = productUserNotificationRepository.findAllByProductId(productId);

            // 매번 유저에게 알림을 전송하기 전에 재고 상태 확인
            while (product.getStockStatus() && !userNotificationList.isEmpty()) {
                // 1초에 500번 제한을 bucket4j로 rate limit 구현
                if (!bucket.tryConsume(1)) continue;

                ProductUserNotification curUser = userNotificationList.poll();
                ProductNotificationHistory productNotificationHistory = new ProductNotificationHistory(curUser.getId(), productId, restockCountNow);
                // 재입고 알림을 보낸 유저를 신청 리스트에서 삭제하고 전송 완료 리스트에 추가
                productNotificationHistoryRepository.save(productNotificationHistory);
                productUserNotificationRepository.delete(curUser);
                lastUserId = userNotificationList.peek().getId();
            }

            // 재고가 남아있는지와 알림 신청 리스트가 비어있는지를 확인하여 그에 맞는 상태를 저장
            if (!product.getStockStatus()) productUserNotificationHistory.updateNotificationStatus("CANCELED_BY_SOLD_OUT");
            else productUserNotificationHistory.updateNotificationStatus("COMPLETED");

            return new ApiResponseDto("알림이 발송 되었습니다", HttpStatus.OK.value());
        } catch (Exception e) {
            // 에러로 인한 재입고 알림 전송 취소 상태 저장
            productUserNotificationHistory.updateNotificationStatus("CANCELED_BY_ERROR");
            // 에러로 인해 알림 전송이 멈출 경우 마지막에 알림을 받지 못한 유저부터 다시 전송 시작
            productUserNotificationController.sendReStockNotificationAdmin(lastUserId);
            return new ApiResponseDto("알림이 발송 되었습니다", HttpStatus.OK.value());
        }
    }
}
