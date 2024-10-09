package org.hanghae99.company.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae99.company.common.entity.TimeStamped;
import org.hanghae99.company.product.entity.Product;
import org.hanghae99.company.review.dto.ReviewRequestDto;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false, name = "review_content")
    private String content;

    @Column(name = "review_image")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "id")
    private Product product;

    public Review(ReviewRequestDto requestDto, Product product) {
        this.userId = requestDto.getUserId();
        this.score = requestDto.getScore();
        this.content = requestDto.getContent();
        this.imageUrl = requestDto.getImageUrl();
        this.product = product;
    }
}