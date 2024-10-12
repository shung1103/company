package org.hanghae99.company.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae99.company.common.entity.TimeStamped;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ProductNotificationHistory")
public class ProductNotificationHistory extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_notification_history_id")
    private Long id;

    @Column(name = "send_user_id", nullable = false)
    private Long sendUserId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long restockCount;

    public ProductNotificationHistory(Long sendUserId, Long productId, Long restockCountNow) {
        this.sendUserId = sendUserId;
        this.productId = productId;
        this.restockCount = restockCountNow;
    }
}
