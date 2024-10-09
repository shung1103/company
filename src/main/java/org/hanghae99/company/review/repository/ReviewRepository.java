package org.hanghae99.company.review.repository;

import org.hanghae99.company.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProductId(Long productId);

    Optional<Object> findByUserId(Long userId);

    List<Review> findAllByProductIdOrderByIdDesc(Long productId);
}
