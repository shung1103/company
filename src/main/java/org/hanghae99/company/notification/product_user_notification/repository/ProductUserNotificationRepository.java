package org.hanghae99.company.notification.product_user_notification.repository;

import org.hanghae99.company.notification.product_user_notification.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Queue;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    Queue<ProductUserNotification> findAllByProductId(Long productId);

    boolean existsByProductId(Long productId);
}
