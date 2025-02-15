package com.amcart.product.model;

import com.amcart.product.dto.CategoryHierarchyView;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryHierarchy {
    private Long id;
    private String name;
    private Long parentCategoryId;
    private Integer level;
    private List<CategoryHierarchy> subCategories;

    public static CategoryHierarchy toCategoryHierarchy(CategoryHierarchyView categoryHierarchyView){
        return CategoryHierarchy.builder()
                .id(categoryHierarchyView.getCategoryId())
                .parentCategoryId(categoryHierarchyView.getParentCategoryId())
                .level(categoryHierarchyView.getLevel())
                .name(categoryHierarchyView.getName())
                .build();
    }
}
