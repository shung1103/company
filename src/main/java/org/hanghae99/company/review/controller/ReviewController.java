package org.hanghae99.company.review.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae99.company.review.dto.ReviewRequestDto;
import org.hanghae99.company.review.dto.ReviewResponseDto;
import org.hanghae99.company.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestPart MultipartFile multipartFile, @RequestBody ReviewRequestDto reviewRequestDto, @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(multipartFile, reviewRequestDto, productId));
    }
}
