package com.dlisaev.cropper.service.interfaces;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.User;


import java.security.Principal;
import java.util.List;

public interface NotificationServiceInterface {
    public List<Notification> getAllUnreadNotificationsForUser(Principal principal);

    public List<Notification> getAllNotificationsForUser(Principal principal);

    public Notification createNotification(NotificationDTO notificationDTO, Principal principal);

    public Notification readNotification(Principal principal, Long notificationId);

    public Notification updateNotification(NotificationDTO notificationDTO, Principal principal, Long notificationId);

    public void deleteNotification(Long notificationId);

    public User getUserByPrincipal(Principal principal);
}
