package dev.felipeazsantos.FoodApp.cart.repository;

import dev.felipeazsantos.FoodApp.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
