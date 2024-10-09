package org.hanghae99.company.product.dto;

import lombok.Getter;
import org.hanghae99.company.product.entity.Product;
import org.hanghae99.company.review.dto.ReviewResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponseDto {
    private Long id;
    private Long totalCount;
    private Double score;
    private Integer cursor;
    private List<ReviewResponseDto> reviews;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.totalCount = product.getReviewCount();
        this.score = product.getScore();
        this.reviews = product.getReviews().stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    public ProductResponseDto(Product product, List<ReviewResponseDto> reviewResponseDtoList) {
        this.totalCount = product.getReviewCount();
        this.score = product.getScore();
        this.reviews = reviewResponseDtoList;
    }
}
