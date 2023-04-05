package com.codegym.repository;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.dto.productDTO.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT NEW com.codegym.model.dto.productDTO.ProductDTO (" +
            "pro.id, " +
            "pro.nameProduct, " +
            "pro.price, " +
            "pro.quantity, " +
            "pro.description," +
            "pro.category," +
            "pro.productAvatar" +
            ") " +
            "FROM Product AS pro "
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
            "OR pro.description like :keySearch "
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
            "WHERE pro.category = :categoryId "
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
            "WHERE pro.category = :category "
    )
    Page<ProductDTO> findProductByCategoryName(Category category, Pageable pageable);


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
            "WHERE pro.nameProduct like :keySearch "
    )
    List<ProductDTO> findProductByNameProduct(@Param("keySearch") String keySearch);


//    Page<Product> findProductOrderByNameProductOrPriceOrQuantity(Pageable pageable);

    default Page<Product> findAll(String keyword, Long categoryId, Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null) {
                Predicate predicateKw = criteriaBuilder.like(root.get("nameProduct"), '%' + keyword + '%');
                predicates.add(predicateKw);
            }

            if (categoryId != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("category").get("id"), categoryId);
                predicates.add(predicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

    }
}
