package com.codegym.service.product;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.dto.productDTO.ProductCreateDTO;
import com.codegym.model.dto.productDTO.ProductDTO;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService extends IGeneralService<Product> {
    List<ProductDTO> findAllByDeletedIsFalse();

    Page<Product> findAllProduct(Pageable pageable);

    Page<ProductDTO> findProductByNameProductOrDescriptionAndDeletedIsFalse(String keySearch, Pageable pageable);

    Page<ProductDTO> findAllByDeletedIsFalse(Pageable pageable);

    boolean existsByNameProduct(String nameProduct);

    Product createWithAvatar(ProductCreateDTO productCreateDTO, Category category);
    Boolean existsByNameProductAndIdNot(String productName, Long id);
    Product saveWithAvatar(Product product, MultipartFile file);

    void softDelete(Long productId);
    List<ProductDTO> findProductByCategory(@Param("categoryId") Long categoryId);

    List<ProductDTO> findProductByCategoryName(@Param("category") Category category);

    List<ProductDTO> findProductByNameProduct(@Param("keySearch")String keySearch);
}