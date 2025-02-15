package com.amcart.product.service;

import com.amcart.product.dto.Category;
import com.amcart.product.dto.Product;
import com.amcart.product.dto.CategoryHierarchyView;
import com.amcart.product.model.CategoryHierarchy;
import com.amcart.product.repository.CategoryRepository;
import com.amcart.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public Page<Product> getProducts(Pageable pageable, String searchText) {
        if (ObjectUtils.isEmpty(searchText))
            return productRepository.findAll(pageable);
        return productRepository.fetchProductsUsingSearchText(pageable, "%" + searchText + "%");
    }

    public Product getProductDetails(String uuid) {
        Product product = productRepository.findById(UUID.fromString(uuid)).orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setCategory(categoryRepository.findById(product.getCategoryId()).orElse(null));
        return product;
    }

    public List<CategoryHierarchy> getProductCategory() {
        List<CategoryHierarchyView> categoryHierarchyViews = categoryRepository.getCategoryHierarchy();
        List<CategoryHierarchy> categoryHierarchies = categoryHierarchyViews.stream().map(CategoryHierarchy::toCategoryHierarchy).collect(Collectors.toList());
        Map<Long, CategoryHierarchy> categoryHierarchiesMap = categoryHierarchies.stream().collect(Collectors.toMap(CategoryHierarchy::getId, categoryHierarchy -> categoryHierarchy));
        categoryHierarchies.stream().forEach(categoryHierarchy -> {
            if (!ObjectUtils.isEmpty(categoryHierarchy.getParentCategoryId())) {
                CategoryHierarchy categoryHierarchy1 = categoryHierarchiesMap.get(categoryHierarchy.getParentCategoryId());
                if (Objects.isNull(categoryHierarchy1.getSubCategories())) {
                    categoryHierarchy1.setSubCategories(Arrays.asList(categoryHierarchy));
                } else {
                    categoryHierarchy1.getSubCategories().add(categoryHierarchy);
                }
            }
        });
        return categoryHierarchiesMap.values().stream().filter(categoryHierarchy -> ObjectUtils.isEmpty(categoryHierarchy.getParentCategoryId())).collect(Collectors.toList());
    }

    @Transactional
    public Product addProduct(Product product) {
        product.setProductId(UUID.randomUUID());
        return productRepository.save(product);
    }
}
