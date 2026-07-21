package dev.felipeazsantos.FoodApp.payment.repository;

import dev.felipeazsantos.FoodApp.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
