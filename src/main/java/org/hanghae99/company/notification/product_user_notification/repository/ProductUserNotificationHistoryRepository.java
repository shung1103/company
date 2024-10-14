package org.hanghae99.company.notification.product_user_notification.repository;

import org.hanghae99.company.notification.product_user_notification.entity.ProductUserNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserNotificationHistoryRepository extends JpaRepository<ProductUserNotificationHistory, Long> {
    boolean existsByProductId(Long productId);

    ProductUserNotificationHistory findByProductId(Long productId);
}
