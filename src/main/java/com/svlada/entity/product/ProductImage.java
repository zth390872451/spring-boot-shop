package com.svlada.entity.product;

import javax.persistence.*;

@Entity
@Table(name = "ProductImage")
public class ProductImage {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Product product;

    private String imageUrl;

    private Integer type;

    public static final Integer MAJOR_IMAGES = 0;
    public static final Integer DETAILS_IMAGES = 1;

    public ProductImage() {
    }

    public ProductImage(Product product, String imageUrl, Integer type) {
        this.product = product;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
