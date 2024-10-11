package org.hanghae99.company.product.repository;

import jakarta.persistence.LockModeType;
import org.hanghae99.company.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Product> findById(Long productId);
}
