package org.hanghae99.company.notification.product_notification.repository;

import org.hanghae99.company.notification.product_notification.entity.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {
}
