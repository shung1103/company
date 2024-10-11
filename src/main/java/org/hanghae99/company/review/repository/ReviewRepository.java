package org.hanghae99.company.review.repository;

import jakarta.persistence.LockModeType;
import org.hanghae99.company.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    List<Review> findAllByProductId(Long productId);

    Page<Review> findAllByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);

    Optional<Object> findByProductIdAndUserId(Long productId, Long userId);

    @Query("SELECT r from Review r where r.id <= ?1 and r.product.id = ?2 order by r.createdAt desc ")
    Page<Review> findReviewNextPage(Long cursor, Long productId, Pageable pageable);
}
