package com.codegym.api;


import com.codegym.exception.DataInputException;
import com.codegym.exception.EmailExistsException;
import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.dto.CategoryDTO;
import com.codegym.model.dto.productDTO.ProductCreateDTO;
import com.codegym.model.dto.productDTO.ProductDTO;
import com.codegym.model.dto.productDTO.ProductUpdateDTO;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import com.codegym.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {

    @Autowired
    IProductService productService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    AppUtils appUtils;


    @GetMapping("/total")
    public ResponseEntity<List<ProductDTO>> getAllProduct() {
        List<ProductDTO> productDTOS = productService.findAllByDeletedIsFalse();

        if (productDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }


//    @GetMapping
//    public ResponseEntity<?> getAllProductPage(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable){
////        Page<Product> products = productService.findAllProduct(pageable);
//
//        Page<ProductDTO> products = productService.findAllByDeletedIsFalse(pageable);
//
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }


//    @GetMapping("/sort")
//    public ResponseEntity<?> getAllProductBySort(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5)Pageable pageable){
//        Page<ProductDTO> productDTOS = productService.findAllByDeletedIsFalse(pageable);
//        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
//    }


    @GetMapping("/category")
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        List<CategoryDTO> categoryDTOS = categoryService.findAllCategoryDTO();
        if (categoryDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getAllProductByFilter(@RequestParam Long categoryId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        if (categoryId == -1) {
            Page<ProductDTO> productDTOS = productService.findAllByDeletedIsFalse(pageable);
            if (productDTOS.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
        }
        Category category = categoryService.findById(categoryId).get();
        Page<ProductDTO> productDTOS = productService.findProductByCategoryName(category, pageable);
        if (productDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllProductBySearch(@RequestParam String keySearch,@RequestParam Long categoryId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        Page<ProductDTO> productDTOS = productService.findAll(keySearch, categoryId, pageable);
        if (productDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getById(@PathVariable Long productId) {

        Optional<Product> optionalProduct = productService.findById(productId);

        if (!optionalProduct.isPresent()) {
            throw new DataInputException("ID hàng hóa không hợp lệ !");
        }

        Product product = optionalProduct.get();
        ProductDTO productDTO = product.toProductDTO();
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@Validated ProductCreateDTO productCreateDTO, BindingResult bindingResult) {
        new ProductCreateDTO().validate(productCreateDTO, bindingResult);
        MultipartFile imageFile = productCreateDTO.getFile();

        if (imageFile == null) {
            throw new DataInputException("Vui lòng chọn hình ảnh!!");
        }

        Optional<Category> categoryOptional = categoryService.findById(productCreateDTO.getCategoryId());

        if (!categoryOptional.isPresent()) {
            throw new DataInputException("Danh mục không hợp lệ");
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (productService.existsByNameProduct(productCreateDTO.getNameProduct())) {
            throw new EmailExistsException("Sản phẩm đã tồn tại trong hệ thống!!");
        }

        Category category = categoryOptional.get();

        productCreateDTO.setId(null);
        Product newProduct = productService.createWithAvatar(productCreateDTO, category);

        return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> update(@PathVariable Long productId, MultipartFile file, @Validated ProductUpdateDTO productUpdateDTO, BindingResult bindingResult) {
        Optional<Product> productOptional = productService.findById(productId);
        if (!productOptional.isPresent()) {
            throw new DataInputException("ID sản phẩm không tồn tại!!");
        }

        Product product = productOptional.get();

        if (productService.existsByNameProductAndIdNot(productUpdateDTO.getNameProduct(), productId)) {
            throw new DataInputException("Sản phẩm đã tồn tại trong hệ thống!!");
        }


        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        Category category = categoryService.findById(Long.valueOf(productUpdateDTO.getCategoryId())).get();
        String productName = productUpdateDTO.getNameProduct();
        BigDecimal price = new BigDecimal(Long.parseLong(productUpdateDTO.getPrice()));
        int quantity = Integer.parseInt(productUpdateDTO.getQuantity());
        String description = productUpdateDTO.getDescription();

        product.setNameProduct(productName)
                .setPrice(price)
                .setQuantity(quantity)
                .setDescription(description)
                .setCategory(category);
        product = productService.save(product);

        if (file != null) {
            product = productService.saveWithAvatar(product, file);
        }
        return new ResponseEntity<>(product.toProductDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> delete(@PathVariable Long productId) {

        Optional<Product> productOptional = productService.findById(productId);

        if (!productOptional.isPresent()) {
            throw new DataInputException("ID sản phẩm không hợp lệ!!");
        }

        try {
            productService.softDelete(productId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            throw new DataInputException("Vui lòng liên hệ Administrator!!");
        }
    }
}
