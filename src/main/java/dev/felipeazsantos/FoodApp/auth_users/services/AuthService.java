package dev.felipeazsantos.FoodApp.auth_users.services;

import dev.felipeazsantos.FoodApp.auth_users.dtos.LoginRequest;
import dev.felipeazsantos.FoodApp.auth_users.dtos.RegistrationRequest;
import dev.felipeazsantos.FoodApp.response.Response;

public interface AuthService {

    Response<?> register(RegistrationRequest registrationRequest);
    Response<?> login(LoginRequest loginRequest);
}
