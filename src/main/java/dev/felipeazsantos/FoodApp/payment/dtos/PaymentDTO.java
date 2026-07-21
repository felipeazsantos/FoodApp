package dev.felipeazsantos.FoodApp.payment.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.felipeazsantos.FoodApp.auth_users.dtos.UserDTO;
import dev.felipeazsantos.FoodApp.enums.PaymentGateway;
import dev.felipeazsantos.FoodApp.enums.PaymentStatus;
import dev.felipeazsantos.FoodApp.order.dtos.OrderDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {

    private Long id;

    private Long orderId;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private String transactionId;

    private PaymentGateway paymentGateway;

    private String failureReason;

    private boolean success;

    private LocalDateTime paymentDate;

    private OrderDTO order;

    private UserDTO user;
}
