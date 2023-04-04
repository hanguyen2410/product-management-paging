package com.codegym.repository;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.dto.productDTO.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT NEW com.codegym.model.dto.productDTO.ProductDTO (" +
                "pro.id, " +
                "pro.nameProduct, " +
                "pro.price, " +
                "pro.quantity, " +
                "pro.description," +
                "pro.category," +
                "pro.productAvatar" +
            ") " +
            "FROM Product AS pro " +
            "WHERE pro.deleted = false "
    )
    List<ProductDTO> findAllProductDTOByDeletedIsFalse();

    @Query("SELECT NEW com.codegym.model.dto.productDTO.ProductDTO (" +
            "pro.id, " +
            "pro.nameProduct, " +
            "pro.price, " +
            "pro.quantity, " +
            "pro.description," +
            "pro.category," +
            "pro.productAvatar" +
            ") " +
            "FROM Product AS pro " +
            "WHERE pro.nameProduct like :keySearch " +
            "OR pro.description like :keySearch " +
            "And pro.deleted = false "
    )
    Page<ProductDTO> findProductByNameProductOrDescriptionAndDeletedIsFalse(String keySearch, Pageable pageable);

    Page<Product> findAllByDeletedIsFalse(Pageable pageable);


    boolean existsByNameProduct(String nameProduct);


    Boolean existsByNameProductAndIdNot(String productName, Long id);


    @Modifying
    @Query("UPDATE Product AS p SET p.deleted = true WHERE p.id = :productId")
    void softDelete(@Param("productId") long productId);


    @Query("SELECT NEW com.codegym.model.dto.productDTO.ProductDTO (" +
                "pro.id, " +
                "pro.nameProduct, " +
                "pro.price, " +
                "pro.quantity, " +
                "pro.description," +
                "pro.category," +
                "pro.productAvatar" +
            ") " +
            "FROM Product AS pro " +
            "WHERE pro.category = :categoryId " +
            "AND pro.deleted = false"
    )
    List<ProductDTO> findProductByCategory(@Param("categoryId") Long categoryId);


    @Query("SELECT NEW com.codegym.model.dto.productDTO.ProductDTO (" +
                "pro.id, " +
                "pro.nameProduct, " +
                "pro.price, " +
                "pro.quantity, " +
                "pro.description," +
                "pro.category," +
                "pro.productAvatar" +
            ") " +
            "FROM Product AS pro " +
            "WHERE pro.category = :category " +
            "AND pro.deleted = false"
    )
    List<ProductDTO> findProductByCategoryName(@Param("category") Category category);


    @Query("SELECT NEW com.codegym.model.dto.productDTO.ProductDTO (" +
                "pro.id, " +
                "pro.nameProduct, " +
                "pro.price, " +
                "pro.quantity, " +
                "pro.description," +
                "pro.category," +
                "pro.productAvatar" +
            ") " +
            "FROM Product AS pro " +
            "WHERE pro.nameProduct like :keySearch " +
            "AND pro.deleted = false "
    )
    List<ProductDTO> findProductByNameProduct(@Param("keySearch")String keySearch);

}
