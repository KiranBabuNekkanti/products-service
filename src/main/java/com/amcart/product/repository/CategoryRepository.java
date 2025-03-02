package com.amcart.product.repository;

import com.amcart.product.dto.Category;
import com.amcart.product.dto.CategoryHierarchyView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

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

    @Query(value = "WITH RECURSIVE CategoryHierarchy AS (\n" +
            "    -- Anchor member: Select top-level categories (level 1)\n" +
            "    SELECT\n" +
            "        category_id,\n" +
            "        \"name\",\n" +
            "        parent_category_id,\n" +
            "        discount_perc,\n" +
            "        discountstartdate,\n" +
            "        discountenddate,\n" +
            "        \"level\"\n" +
            "    FROM\n" +
            "        products.category\n" +
            "    WHERE\n" +
            "        parent_category_id in (:categoryIds)\n" +
            "\n" +
            "    UNION ALL\n" +
            "\n" +
            "    -- Recursive member: Join with parent categories to build the hierarchy\n" +
            "    SELECT\n" +
            "        c.category_id,\n" +
            "        c.\"name\",\n" +
            "        c.parent_category_id,\n" +
            "        c.discount_perc,\n" +
            "        c.discountstartdate,\n" +
            "        c.discountenddate,\n" +
            "        c.\"level\"\n" +
            "    FROM\n" +
            "        products.category c\n" +
            "        JOIN CategoryHierarchy ch ON c.parent_category_id = ch.category_id\n" +
            ")select distinct category_id from CategoryHierarchy", nativeQuery = true)
    Set<Long> getSubCategoryIds(@Param("categoryIds")List<Long> categoryIds);
}
