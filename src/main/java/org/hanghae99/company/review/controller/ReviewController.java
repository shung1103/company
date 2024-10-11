package org.hanghae99.company.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanghae99.company.review.dto.ReviewResponseDto;
import org.hanghae99.company.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable Long productId, @RequestPart(required = false) MultipartFile multipartFile, @RequestParam("reviewRequestDto") String reviewRequestDto) throws InterruptedException {
        while (true) {
            try {
                return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(productId, multipartFile, reviewRequestDto));
            } catch (ObjectOptimisticLockingFailureException e) {
                Thread.sleep(50);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
