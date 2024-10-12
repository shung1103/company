package org.hanghae99.company.product.repository;

import org.hanghae99.company.product.entity.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {
}
