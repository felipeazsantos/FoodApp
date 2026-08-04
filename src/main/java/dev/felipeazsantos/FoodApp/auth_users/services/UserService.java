package dev.felipeazsantos.FoodApp.auth_users.services;

import dev.felipeazsantos.FoodApp.auth_users.dtos.UserDTO;
import dev.felipeazsantos.FoodApp.auth_users.entity.User;
import dev.felipeazsantos.FoodApp.response.Response;

import java.util.List;

public interface UserService {
    User getCurrentLoggedInUser();

    Response<List<UserDTO>> getAllUsers();

    Response<UserDTO> getOwnAccountDetails();

    Response<?> updateOwnAccount(UserDTO userDTO);

    Response<?> deactivateOwnAccount();
}
