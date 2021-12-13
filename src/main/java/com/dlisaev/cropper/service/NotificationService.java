package com.dlisaev.cropper.service;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.dto.OfferDTO;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.exceptions.*;
import com.dlisaev.cropper.repository.CropRepository;
import com.dlisaev.cropper.repository.NotificationRepository;
import com.dlisaev.cropper.repository.OfferRepository;
import com.dlisaev.cropper.repository.UserRepository;
import com.dlisaev.cropper.service.interfaces.NotificationServiceInterface;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class NotificationService implements NotificationServiceInterface {
    public static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }


    public List<Notification> getAllUnreadNotificationsForUser(Principal principal){
        User user = getUserByPrincipal(principal);
        return notificationRepository.findAllByUserToAndHasReadOrderByCreateDate(user, false);
    }

    public List<Notification> getAllNotificationsForUser(Principal principal){
        User user = getUserByPrincipal(principal);
        return notificationRepository.findAllByUserToOrderByCreateDate(user);
    }

    public Notification createNotification(NotificationDTO notificationDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        if (!notificationDTO.getUsernameTo().equals(user.getUsername())) {
            Notification notification = new Notification();

            notification.setUserFrom(user);
            User userTo = userRepository.findUserByUsername(notificationDTO.getUsernameTo())
                    .orElseThrow(() -> new UsernameNotFoundException("User-destination not found with username" + notificationDTO.getUsernameTo()));
            notification.setUserTo(userTo);
            notification.setTitle(notificationDTO.getTitle());
            notification.setMessage(notification.getMessage());
            notification.setHasRead(false);

            LOG.info("Create new notification from " + user.getEmail() + " for user: {}", userTo.getEmail());
            return notificationRepository.save(notification);
        } else {
            LOG.error("Error! You can`t send message for yourself! {}", user.getUsername());
            throw new NotificationCantBeSendForYourself("Message can`t be sent to yourself! " + user.getUsername());
        }
    }

    public Notification readNotification(Principal principal, Long notificationId){
        User user = getUserByPrincipal(principal);

        Notification notification = notificationRepository.findNotificationByIdAndUserTo(notificationId, user)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + notificationId));

        notification.setHasRead(true);

        return notificationRepository.save(notification);
    }


    public Notification updateNotification(NotificationDTO notificationDTO, Principal principal, Long notificationId){
        User user = getUserByPrincipal(principal);

        Notification notification = notificationRepository.findNotificationByIdAndUserTo(notificationId, user)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + notificationId));

        notification.setMessage(notificationDTO.getMessage());
        notification.setTitle(notification.getTitle());

        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId){
        Notification notification = notificationRepository.getById(notificationId);
        notificationRepository.delete(notification);
    }


    public User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
