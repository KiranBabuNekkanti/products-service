package com.amcart.product.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "category_id")
    Long categoryId;

    @Column(name = "name")
    String name;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    Category parentCategory;

    @Column(name = "discount_perc")
    Integer discountPerc;

    @Column(name = "discountstartdate")
    LocalDate discountStartDate;

    @Column(name = "discountenddate")
    LocalDate discountEndDate;

    @Column(name = "level")
    Integer level;
}
