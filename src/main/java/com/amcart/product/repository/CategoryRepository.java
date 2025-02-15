package com.amcart.product.repository;

import com.amcart.product.dto.Category;
import com.amcart.product.dto.CategoryHierarchyView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "WITH RECURSIVE category_hierarchy AS (\n" +
            "    SELECT category_id, name, parent_category_id, level\n" +
            "    FROM {h-schema}category\n" +
            "    WHERE parent_category_id is null\n" +
            "\n" +
            "    UNION ALL\n" +
            "\n" +
            "    SELECT c.category_id, c.name, c.parent_category_id, c.level\n" +
            "    FROM {h-schema}category c\n" +
            "    INNER JOIN category_hierarchy ch ON c.parent_category_id = ch.category_id\n" +
            ")\n" +
            "SELECT category_id as categoryId, name as name, parent_category_id as parentCategoryId, level as level FROM category_hierarchy", nativeQuery = true)
    List<CategoryHierarchyView> getCategoryHierarchy();
}
