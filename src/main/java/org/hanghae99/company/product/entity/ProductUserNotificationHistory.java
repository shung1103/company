package org.hanghae99.company.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae99.company.common.entity.TimeStamped;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Product_user_notification_history")
public class ProductUserNotificationHistory extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String status;

    public ProductUserNotificationHistory(Long productId, String status) {
        this.productId = productId;
        this.status = status;
    }

    public void updateNotificationStatus(String status) {
        this.status = status;
    }
}
