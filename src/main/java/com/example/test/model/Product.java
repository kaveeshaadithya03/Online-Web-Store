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

    @NotNull(message = "Product Price is required")
    @Positive(message = "Product Price must be positive")
    private Double productPrice;

    @NotNull(message = "Product Count is required")
    @Positive(message = "Product Count must be positive")
    private Integer productCount;

    public Product() {}

    public Product(String productID, String productName, Double productPrice, Integer productCount) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCount = productCount;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Double getProductPrice() { return productPrice; }
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }
    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
}