package com.amcart.product.service;

import com.amcart.product.dto.Product;
import com.amcart.product.dto.CategoryHierarchyView;
import com.amcart.product.model.CategoryHierarchy;
import com.amcart.product.repository.CategoryRepository;
import com.amcart.product.repository.ProductRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public Page<Product> getProducts(Pageable pageable, String searchText, List<Long> categoryIds) {
        if (ObjectUtils.isEmpty(searchText) && ObjectUtils.isEmpty(categoryIds))
            return productRepository.findAll(pageable);
        searchText = ObjectUtils.isEmpty(searchText) ? null : ("%" + searchText + "%");
        Set<Long> subCategoryIds = ObjectUtils.isEmpty(categoryIds) ? null : categoryRepository.getSubCategoryIds(categoryIds);
        if(!ObjectUtils.isEmpty(categoryIds)){
            subCategoryIds.addAll(categoryIds);
            categoryIds = subCategoryIds.stream().toList();
        }
        return productRepository.fetchProductsUsingSearchTextAndCategories(pageable, searchText, categoryIds);
    }

    public Product getProductDetails(String uuid) {
        Product product = productRepository.findById(UUID.fromString(uuid)).orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setCategory(categoryRepository.findById(product.getCategoryId()).orElse(null));
        return product;
    }

    public String getProductCategory() {
        List<CategoryHierarchyView> categoryHierarchyViews = categoryRepository.getCategoryHierarchy();
        List<CategoryHierarchy> categoryHierarchies = categoryHierarchyViews.stream().map(CategoryHierarchy::toCategoryHierarchy).collect(Collectors.toList());
        JsonObject object = buildCategoryTree(categoryHierarchies);
        return object.toString();
    }

    private static JsonObject buildCategoryTree(List<CategoryHierarchy> categories) {
        Map<Long, CategoryHierarchy> categoryMap = new HashMap<>();
        for (CategoryHierarchy category : categories) {
            categoryMap.put(category.getId(), category);
        }

        JsonObject root = new JsonObject();
        JsonArray rootArray = new JsonArray();

        for (CategoryHierarchy category : categories) {
            if (category.getParentCategoryId() == null) {
                rootArray.add(buildCategoryJson(category, categoryMap));
            }
        }
        root.add("filters", rootArray);
        return root;
    }

    private static JsonObject buildCategoryJson(CategoryHierarchy category, Map<Long, CategoryHierarchy> categoryMap) {
        JsonObject json = new JsonObject();
        json.addProperty("category_id", category.getId());
        json.addProperty("name", category.getName());
        //json.addProperty("discount_perc", category.);
        //json.addProperty("discountstartdate", category.discountStartDate != null ? category.discountStartDate.toString() : null);
        //json.addProperty("discountenddate", category.discountEndDate != null ? category.discountEndDate.toString() : null);

        JsonArray subcategoriesJson = new JsonArray();
        for (CategoryHierarchy subCategory : categoryMap.values()) {
            if (subCategory.getParentCategoryId() != null && subCategory.getParentCategoryId().equals(category.getId())) {
                subcategoriesJson.add(buildCategoryJson(subCategory, categoryMap));
            }
        }
        json.add("subcategories", subcategoriesJson);
        return json;
    }

    @Transactional
    public Product addProduct(Product product) {
        product.setProductId(UUID.randomUUID());
        return productRepository.save(product);
    }
}
