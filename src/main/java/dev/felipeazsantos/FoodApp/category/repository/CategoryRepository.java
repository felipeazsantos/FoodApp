package dev.felipeazsantos.FoodApp.category.repository;

import dev.felipeazsantos.FoodApp.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
