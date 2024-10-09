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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewResponseDto createReview(Long productId, MultipartFile multipartFile, String reviewRequestDto_temp) throws JsonProcessingException {
        ReviewRequestDto reviewRequestDto = conversionDto(reviewRequestDto_temp);

        if (reviewRepository.findByUserId(reviewRequestDto.getUserId()).isPresent()) throw new IllegalArgumentException("해당 상품에 이미 리뷰를 작성하셨습니다.");

        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));

        String filename = null;
        if (multipartFile != null) filename = multipartFile.getOriginalFilename();
        reviewRequestDto.setImageUrl(filename);
        Review review = new Review(reviewRequestDto, product);
        reviewRepository.save(review);

        List<Review> reviewList = reviewRepository.findAllByProductId(productId);
        long totalCount = reviewList.size();
        double sum = 0.0;
        for (Review reviewItem : reviewList) sum += reviewItem.getScore();
        double average = sum / totalCount;
        product.update(totalCount, average);
        productRepository.save(product);

        return new ReviewResponseDto(review);
    }

    //json타입으로 변환
    public ReviewRequestDto conversionDto(String reviewRequestDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reviewRequestDto, ReviewRequestDto.class);
    }
}
