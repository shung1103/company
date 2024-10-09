package org.hanghae99.company.product.service;

import lombok.RequiredArgsConstructor;
import org.hanghae99.company.common.dto.ApiResponseDto;
import org.hanghae99.company.product.dto.ProductRequestDto;
import org.hanghae99.company.product.dto.ProductResponseDto;
import org.hanghae99.company.product.entity.Product;
import org.hanghae99.company.product.repository.ProductRepository;
import org.hanghae99.company.review.dto.ReviewResponseDto;
import org.hanghae99.company.review.entity.Review;
import org.hanghae99.company.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product(productRequestDto);
        productRepository.save(product);
        return new ProductResponseDto(product);
    }

    public ProductResponseDto getProductInfo(Long productId, Pageable pageable, Long cursor) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("해당 번호의 상품이 존재하지 않습니다."));

        Page<Review> reviews;
        if (cursor == null) {
            reviews = reviewRepository.findAllByProductIdOrderByCreatedAtDesc(productId, pageable);
        }
        else {
            reviews = reviewRepository.findReviewNextPage(cursor, productId, pageable);
        }

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        for (Review review : reviews) reviewResponseDtoList.add(new ReviewResponseDto(review));

        return new ProductResponseDto(product, reviewResponseDtoList);
    }

    public ApiResponseDto deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));
        productRepository.delete(product);
        return new ApiResponseDto("상품이 삭제 되었습니다.", HttpStatus.OK.value());
    }
}
