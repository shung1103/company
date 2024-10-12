package org.hanghae99.company.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae99.company.common.entity.TimeStamped;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Product")
public class Product extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long restockCount;

    public void updateReStockCount(Long restockCount) { this.restockCount = restockCount; }

    public void updateQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
