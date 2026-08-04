package dev.felipeazsantos.FoodApp.auth_users.services;

import dev.felipeazsantos.FoodApp.auth_users.dtos.UserDTO;
import dev.felipeazsantos.FoodApp.auth_users.entity.User;
import dev.felipeazsantos.FoodApp.auth_users.repository.UserRepository;
import dev.felipeazsantos.FoodApp.aws.AwsS3Service;
import dev.felipeazsantos.FoodApp.email_notification.dtos.NotificationDTO;
import dev.felipeazsantos.FoodApp.email_notification.entity.Notification;
import dev.felipeazsantos.FoodApp.email_notification.services.NotificationService;
import dev.felipeazsantos.FoodApp.exception.BadRequestException;
import dev.felipeazsantos.FoodApp.exception.NotFoundException;
import dev.felipeazsantos.FoodApp.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private final AwsS3Service awsS3Service;

    @Override
    public User getCurrentLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    @Override
    public Response<List<UserDTO>> getAllUsers() {
        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<UserDTO> userDTOS = modelMapper.map(userList, new TypeToken<List<UserDTO>>() {
        }.getType());

        return Response.<List<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("All users retrieved successfully")
                .data(userDTOS)
                .build();
    }

    @Override
    public Response<UserDTO> getOwnAccountDetails() {
        User user = getCurrentLoggedInUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("user retrieved successful")
                .data(userDTO)
                .build();
    }

    @Override
    public Response<?> updateOwnAccount(UserDTO userDTO) {
        User user = getCurrentLoggedInUser();

        String profileUrl = user.getProfileUrl();

        MultipartFile imageFile = userDTO.getImageFile();

        if (imageFile != null && !imageFile.isEmpty()) {
            if (profileUrl != null && !profileUrl.isEmpty()) {
                String keyName = profileUrl.substring(profileUrl.lastIndexOf("/") + 1);
                awsS3Service.DeleteFile("profile/" + keyName);
                log.info("Deleted old image from cloud");
            }

            String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            URL newImageURL = awsS3Service.uploadFile("profile/" + imageName, imageFile);
            user.setProfileUrl(newImageURL.toString());
        }

        if (userDTO.getName() != null) user.setName(userDTO.getName());
        if (userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getAddress() != null) user.setAddress(userDTO.getAddress());
        if (userDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new BadRequestException("Email already exists");
            }
        }

        user.setEmail(userDTO.getEmail());

        userRepository.save(user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account updated successfully")
                .build();
    }

    @Override
    public Response<?> deactivateOwnAccount() {
        User user = getCurrentLoggedInUser();

        user.setIsActive(false);
        userRepository.save(user);

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Account Deactivated")
                .body("Your account has been deactivated. If this was a mistake, please contact support.")
                .build();

        notificationService.sendEmail(notificationDTO);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account deactivated successfully")
                .build();
    }
}
