package com.codegym.model.dto.productDTO;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.ProductAvatar;
import com.codegym.model.dto.CategoryDTO;
import com.codegym.model.dto.ProductAvatarDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long id;

    private String nameProduct;

    private BigDecimal price;

    private int quantity;

    private String description;

    private CategoryDTO category;

    private ProductAvatarDTO productAvatar;

    public ProductDTO(Long id, String nameProduct, BigDecimal price, int quantity, String description, Category category, ProductAvatar productAvatar) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category.toCategoryDTO();
        this.productAvatar = productAvatar.toProductAvatarDTO();
    }

    private Boolean deleted;

public Product toProduct(){
    return new Product()
            .setId(id)
            .setNameProduct(nameProduct)
            .setPrice(price)
            .setQuantity(quantity)
            .setCategory(category.toCategory())
            .setDescription(description);
}
}
