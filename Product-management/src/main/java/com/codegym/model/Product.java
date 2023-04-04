package com.codegym.model;

import com.codegym.model.dto.productDTO.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
@Where(clause = "deleted = false")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameProduct;


    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal price;

    private int  quantity;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "product_avatar_id")
    private ProductAvatar productAvatar;

    public ProductDTO toProductDTO() {
        return new ProductDTO()
                .setId(id)
                .setNameProduct(nameProduct)
                .setPrice(price)
                .setQuantity(quantity)
                .setCategory(category.toCategoryDTO())
                .setDescription(description)
                .setProductAvatar(productAvatar.toProductAvatarDTO())
                ;
    }

}
