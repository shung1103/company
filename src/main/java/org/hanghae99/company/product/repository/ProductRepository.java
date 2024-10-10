package org.hanghae99.company.product.repository;

import org.hanghae99.company.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
