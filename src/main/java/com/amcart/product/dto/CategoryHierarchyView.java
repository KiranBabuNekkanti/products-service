package com.amcart.product.dto;

import java.io.Serializable;

public interface CategoryHierarchyView extends Serializable {
    Long getCategoryId();

    String getName();

    Long getParentCategoryId();

    Integer getLevel();
}
