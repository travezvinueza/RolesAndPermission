package com.develop.backend.domain.repository;

import com.develop.backend.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);
    Product findTopByOrderByIdDesc();

    @Modifying
    @Query("update Product p set p.stock = p.stock - :quantity where p.id = :productId and p.stock >= :quantity")
    int reduceInventory(@Param("productId") Long productId, @Param("quantity") int quantity);

    @Modifying
    @Query("update Product p set p.stock = p.stock + :quantity where p.id = :productId")
    int addStock(@Param("productId") Long productId, @Param("quantity") int quantity);
}
