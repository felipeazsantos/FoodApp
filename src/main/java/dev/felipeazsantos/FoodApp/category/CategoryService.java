package dev.felipeazsantos.FoodApp.category;

import dev.felipeazsantos.FoodApp.category.dtos.CategoryDTO;
import dev.felipeazsantos.FoodApp.response.Response;

import java.util.List;

public interface CategoryService {

    Response<CategoryDTO> addCategory(CategoryDTO categoryDTO);
    Response<CategoryDTO> updateCategory(CategoryDTO categoryDTO);

    Response<List<CategoryDTO>> getAllCategories();
    Response<CategoryDTO> getCategoryById(Long id);
    Response<?> deleteCategory(Long id);

}
