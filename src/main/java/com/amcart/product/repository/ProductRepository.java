package com.amcart.product.repository;

import com.amcart.product.dto.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductRepository extends PagingAndSortingRepository<Product, UUID>, JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.name ILIKE %:searchText% OR p.description ILIKE %:searchText%")
    Page<Product> fetchProductsUsingSearchText(Pageable pageable, @Param("searchText") String searchText);
}
