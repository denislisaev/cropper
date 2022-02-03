package com.dlisaev.cropper.facade;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.dto.NotificationInputDTO;
import com.dlisaev.cropper.dto.PresentNotificationDTO;
import com.dlisaev.cropper.entity.Notification;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;


@Component
public class NotificationFacade {
    public NotificationDTO notificationToNotificationDTO(Notification notification){
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setTitle(notification.getTitle());
        notificationDTO.setUsernameTo(notification.getUserTo().getUsername());

        return notificationDTO;
    }

    public PresentNotificationDTO notificationToPresentNotificationDTO(Notification notification){
        PresentNotificationDTO presentNotificationDTO = new PresentNotificationDTO();

        presentNotificationDTO.setMessage(notification.getMessage());
        presentNotificationDTO.setTitle(notification.getTitle());
        presentNotificationDTO.setUsernameFrom(notification.getUserFrom().getUsername());
        presentNotificationDTO.setUsernameTo(notification.getUserTo().getUsername());
        presentNotificationDTO.setHasRead(notification.getHasRead());

        return presentNotificationDTO;
    }

    public NotificationInputDTO notificationToNotificationInputDTO(Notification notification){
        NotificationInputDTO notificationDTO = new NotificationInputDTO();

        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setTitle(notification.getTitle());
        notificationDTO.setUsernameFrom(notification.getUserFrom().getUsername());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        notificationDTO.setCreateDate(notification.getCreateDate().format(formatter));

        return notificationDTO;
    }
}
