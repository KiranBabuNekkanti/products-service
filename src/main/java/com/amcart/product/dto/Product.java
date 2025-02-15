package com.amcart.product.dto;

import com.amcart.product.config.ImageModelAttributeConverter;
import com.amcart.product.model.ImageModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @Column(name = "product_id")
    UUID productId;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "price")
    float price;

    @Column(name = "stock")
    Integer stock;

    @Column(name = "return_policy")
    String returnPolicy;

    @Column(name = "no_of_days_for_return")
    Integer returnWindow;

    @Convert(converter = ImageModelAttributeConverter.class)
    @Column(name = "images", columnDefinition = "jsonb")
    List<ImageModel> images;

    @Column(name = "category_id")
    Long categoryId;

    @Transient
    Category category;

}
