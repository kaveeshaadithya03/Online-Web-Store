package com.example.test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @NotBlank(message = "Product ID is required")
    private String productID;

    @NotBlank(message = "Product Name is required")
    private String productName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Product Price is required")
    @Positive(message = "Product Price must be positive")
    private Double productPrice;

    @NotNull(message = "Product Count is required")
    @Positive(message = "Product Count must be positive")
    private Integer productCount;

    private String images;

    @NotBlank(message = "Seller ID is required")
    private String sellerId;

    public Product() {}

    public Product(String productID, String productName, String description, Double productPrice, Integer productCount, String images, String sellerId) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.productPrice = productPrice;
        this.productCount = productCount;
        this.images = images;
        this.sellerId = sellerId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getProductPrice() { return productPrice; }
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }
    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
}