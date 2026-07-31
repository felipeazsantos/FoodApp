package dev.felipeazsantos.FoodApp.aws;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface AwsS3Service {
    URL uploadFile(String keyName, MultipartFile file);
    void DeleteFile(String keyName);
}
