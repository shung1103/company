package org.hanghae99.company.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hanghae99.company.product.entity.Product;
import org.hanghae99.company.product.repository.ProductRepository;
import org.hanghae99.company.review.dto.ReviewRequestDto;
import org.hanghae99.company.review.dto.ReviewResponseDto;
import org.hanghae99.company.review.entity.Review;
import org.hanghae99.company.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ReviewResponseDto createReview(Long productId, MultipartFile multipartFile, String reviewRequestDto_temp) throws JsonProcessingException {
        ReviewRequestDto reviewRequestDto = conversionDto(reviewRequestDto_temp);

        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));
        for (Review r : product.getReviews()) {
            if (r.getUserId().equals(reviewRequestDto.getUserId())) {
                throw new IllegalArgumentException("해당 상품에 이미 리뷰를 작성하셨습니다.");
            }
        }

        String filename = null;
        if (multipartFile != null) filename = multipartFile.getOriginalFilename();
        reviewRequestDto.setImageUrl(filename);
        Review review = new Review(reviewRequestDto, product);
        reviewRepository.save(review);

        long totalCountNow = product.getReviews().size();
        double averageNow = product.getScore();
        product.update(totalCountNow + 1, Math.round((((averageNow * totalCountNow) + review.getScore()) / (totalCountNow + 1)) * 10) / 10.0);

        return new ReviewResponseDto(review);
    }

    //json타입으로 변환
    public ReviewRequestDto conversionDto(String reviewRequestDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reviewRequestDto, ReviewRequestDto.class);
    }
}
