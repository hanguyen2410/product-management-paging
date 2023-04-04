package com.codegym.repository;

import com.codegym.model.Category;
import com.codegym.model.dto.CategoryDTO;
import com.codegym.model.dto.productDTO.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT NEW com.codegym.model.dto.CategoryDTO (" +
                "cat.id, " +
                "cat.title" +
            ")" +
            "FROM Category AS cat "
    )
    List<CategoryDTO> findAllCategoryDTO();

}
