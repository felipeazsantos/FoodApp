package dev.felipeazsantos.FoodApp.category;

import dev.felipeazsantos.FoodApp.category.dtos.CategoryDTO;
import dev.felipeazsantos.FoodApp.category.entity.Category;
import dev.felipeazsantos.FoodApp.category.repository.CategoryRepository;
import dev.felipeazsantos.FoodApp.exception.NotFoundException;
import dev.felipeazsantos.FoodApp.response.Response;
import org.modelmapper.ModelMapper;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response<CategoryDTO> addCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository.save(category);
        return Response.<CategoryDTO>builder().message("Category added successfully").build();
    }

    @Override
    public Response<CategoryDTO> updateCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.getId()).orElseThrow(() -> new NotFoundException("Category not found"));


        if (categoryDTO.getName() != null && !categoryDTO.getName().isEmpty()) {
            category.setName(categoryDTO.getName());
        }

        if (categoryDTO.getDescription() != null && !categoryDTO.getDescription().isEmpty()) {
            category.setDescription(categoryDTO.getDescription());
        }

        categoryRepository.save(category);
        return Response.<CategoryDTO>builder().message("Category updated successfully").data(modelMapper.map(category, CategoryDTO.class)).build();
    }

    @Override
    public Response<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return Response.<List<CategoryDTO>>builder()
                .data(categories.stream()
                                .map(category -> modelMapper.map(category, CategoryDTO.class))
                                .toList())
                .build();
    }

    @Override
    public Response<CategoryDTO> getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));

        return Response.<CategoryDTO>builder().data(modelMapper.map(category, CategoryDTO.class)).build();
    }

    @Override
    public Response<?> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category Not Found");
        }

        categoryRepository.deleteById(id);
        return Response.builder()
                .message("Category deleted")
                .build();
    }
}
