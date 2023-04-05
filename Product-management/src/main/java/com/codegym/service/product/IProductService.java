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

import java.util.List;


public interface IProductService extends IGeneralService<Product> {
    List<ProductDTO> findAllByDeletedIsFalse();


    Page<ProductDTO> findProductByNameProductOrDescriptionAndDeletedIsFalse(String keySearch, Pageable pageable);

    Page<ProductDTO> findAllByDeletedIsFalse(Pageable pageable);

    boolean existsByNameProduct(String nameProduct);

    Product createWithAvatar(ProductCreateDTO productCreateDTO, Category category);

    Boolean existsByNameProductAndIdNot(String productName, Long id);

    Product saveWithAvatar(Product product, MultipartFile file);

    void softDelete(Long productId);

    List<ProductDTO> findProductByCategory(@Param("categoryId") Long categoryId);

    Page<ProductDTO> findProductByCategoryName(Category category, Pageable pageable);

    //    Page<ProductDTO> findProductOrderByNameProductOrPriceOrQuantity(Pageable pageable);
    Page<ProductDTO> findAll(String keyword, Long categoryId, Pageable pageable);
}
