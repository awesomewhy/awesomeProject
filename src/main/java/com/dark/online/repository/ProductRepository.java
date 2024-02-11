package com.dark.online.repository;

import com.dark.online.dto.product.ProductForShowDto;
import com.dark.online.entity.Order;
import com.dark.online.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductById(Long id);
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')")
    List<Product> findProductByName(@Param("name") String name);

    @NotNull
    @EntityGraph(value = "productWithSellerAndPhoto", type = EntityGraph.EntityGraphType.LOAD)
    List<Product> findAll();

    @Query("SELECT p.id, p.name, p.sellerId.id, p.photoId.imageData, p.rating FROM Product p")
    @EntityGraph(value = "productWithSellerAndPhoto", type = EntityGraph.EntityGraphType.LOAD)
    List<ProductForShowDto> findAllById();
}
