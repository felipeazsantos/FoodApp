package dev.felipeazsantos.FoodApp.auth_users.entity;

import dev.felipeazsantos.FoodApp.cart.entity.Cart;
import dev.felipeazsantos.FoodApp.order.entity.Order;
import dev.felipeazsantos.FoodApp.payment.entity.Payment;
import dev.felipeazsantos.FoodApp.review.entity.Review;
import dev.felipeazsantos.FoodApp.role.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    @Email(message = "email invalid")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
    private String phoneNumber;
    private String profileUrl;
    private String address;
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
