package dev.felipeazsantos.FoodApp.email_notification.services;

import dev.felipeazsantos.FoodApp.email_notification.dtos.NotificationDTO;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO);
}
