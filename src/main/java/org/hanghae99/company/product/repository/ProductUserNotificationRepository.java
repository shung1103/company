package org.hanghae99.company.product.repository;

import org.hanghae99.company.product.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Queue;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    Queue<ProductUserNotification> findAllByProductId(Long productId);
}
