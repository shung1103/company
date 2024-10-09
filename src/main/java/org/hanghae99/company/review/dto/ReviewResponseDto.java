package org.hanghae99.company.review.dto;

import lombok.Getter;
import org.hanghae99.company.review.entity.Review;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long id;
    private Long userId;
    private Integer score;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.userId = review.getUserId();
        this.score = review.getScore();
        this.content = review.getContent();
        this.imageUrl = review.getImageUrl();
        this.createdAt = review.getCreatedAt();
    }
}
