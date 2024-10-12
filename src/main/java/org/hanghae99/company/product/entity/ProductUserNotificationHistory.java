package org.hanghae99.company.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ProductUserNotificationHistory")
public class ProductUserNotificationHistory {
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
