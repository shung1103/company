package org.hanghae99.company.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae99.company.common.entity.TimeStamped;
import org.hanghae99.company.product.dto.ProductRequestDto;
import org.hanghae99.company.review.entity.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Product")
public class Product extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reviewCount", nullable = false)
    private Long reviewCount;

    @Column(name = "score", nullable = false)
    private Double score;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<Review> reviews  = new ArrayList<>();

    public Product(ProductRequestDto productRequestDto) {
        this.reviewCount = productRequestDto.getReviewCount();
        this.score = productRequestDto.getScore();
    }

    public void update(long totalCount, double average) {
        this.reviewCount = totalCount;
        this.score = average;
    }
}
