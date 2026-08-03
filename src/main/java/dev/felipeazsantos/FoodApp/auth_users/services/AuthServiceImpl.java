package dev.felipeazsantos.FoodApp.auth_users.services;

import dev.felipeazsantos.FoodApp.auth_users.dtos.LoginRequest;
import dev.felipeazsantos.FoodApp.auth_users.dtos.LoginResponse;
import dev.felipeazsantos.FoodApp.auth_users.dtos.RegistrationRequest;
import dev.felipeazsantos.FoodApp.auth_users.entity.User;
import dev.felipeazsantos.FoodApp.auth_users.repository.UserRepository;
import dev.felipeazsantos.FoodApp.exception.BadRequestException;
import dev.felipeazsantos.FoodApp.exception.NotFoundException;
import dev.felipeazsantos.FoodApp.response.Response;
import dev.felipeazsantos.FoodApp.role.entity.Role;
import dev.felipeazsantos.FoodApp.role.repository.RoleRepository;
import dev.felipeazsantos.FoodApp.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;

    @Override
    public Response<?> register(RegistrationRequest registrationRequest) {

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        List<Role> userRoles;

        if (registrationRequest.getRoles() != null && !registrationRequest.getRoles().isEmpty()) {
            userRoles = registrationRequest.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName.toUpperCase())
                            .orElseThrow(() -> new NotFoundException("Role with name : " + roleName + " does not exists")))
                    .toList();
        } else {
            Role defaultRole = roleRepository.findByName("CUSTOMER")
                    .orElseThrow(() -> new NotFoundException("Default CUSTOMER role not found"));
            userRoles = List.of(defaultRole);
        }

        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .phoneNumber(registrationRequest.getPhoneNumber())
                .address(registrationRequest.getAddress())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(userRoles)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        log.info("user registered successfully");

        return Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("User Registered Successfully")
                .build();
    }

    @Override
    public Response<?> login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getIsActive()) {
            throw new NotFoundException("Account not active, Please contact customer support");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRoles(roleNames);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Successful")
                .data(loginResponse)
                .build();
    }
}
