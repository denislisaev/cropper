package com.dlisaev.cropper.facade;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.dto.OfferDTO;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.Offer;
import org.springframework.stereotype.Component;

@Component
public class NotificationFacade {
    public NotificationDTO notificationToNotificationDTO(Notification notification){
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setTitle(notification.getTitle());
        notificationDTO.setUsernameTo(notification.getUserTo().getUsername());

        return notificationDTO;
    }
}
