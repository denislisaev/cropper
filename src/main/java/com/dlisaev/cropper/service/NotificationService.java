package com.dlisaev.cropper.service;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.exceptions.*;
import com.dlisaev.cropper.repository.NotificationRepository;
import com.dlisaev.cropper.repository.UserRepository;
import com.dlisaev.cropper.service.interfaces.NotificationServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
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
        try {
            LOG.debug("Find unread notifications for user with username: " + user.getUsername());
            return notificationRepository.findAllByUserToAndHasReadOrderByCreateDate(user, false);
        } catch (Exception e){
            LOG.warn("Not found unread notifications for user with username: " + user.getUsername());
        }
        return new ArrayList<>();
    }

    public List<Notification> getAllNotificationsForUserByUsername(String username){
        try {
            LOG.debug("Find user with username: " + username);
            User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
            LOG.debug("Find notifications for user with username: " + username);
            return notificationRepository.findAllByUserToOrderByCreateDate(user);
        } catch (UsernameNotFoundException e){
            LOG.error(e.getMessage());
        } catch (Exception e){
            LOG.warn("Notifications for user with username " + username + " not found");
        }
        return new ArrayList<>();
    }

    public List<Notification> getAllNotificationsForUserFromByUsername(String username){
        try {
            LOG.debug("Find user with username: " + username);
            User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("User with username " + username + " not found"));
            LOG.debug("Find notifications for user with username: " + username);
            return notificationRepository.findAllByUserFromOrderByCreateDate(user);
        } catch (UsernameNotFoundException e){
            LOG.error(e.getMessage());
        } catch (Exception e){
            LOG.warn("Notifications for userFrom with username " + username + " not found");
        }
        return new ArrayList<>();
    }

    public List<Notification> getAllNotificationsForUser(Principal principal){
        User user = getUserByPrincipal(principal);
        try{
            LOG.debug("Find notifications for user with username: " + user.getUsername());
            return notificationRepository.findAllByUserToOrderByCreateDate(user);
        } catch (Exception e){
            LOG.warn("Notifications for user with username " + user.getUsername() + " not found");
        }
        return new ArrayList<>();
    }

    public List<Notification> getAllNotificationsForUserToUser(Principal principal, String username){
        User user = getUserByPrincipal(principal);

        try {
            LOG.debug("Find notifications for user "+ user.getUsername() + " for user " + username);
            List<Notification> notifs = notificationRepository.findAllByUserToOrderByCreateDate(user);
            notifs.addAll(notificationRepository.findAllByUserFromOrderByCreateDate(user));
            notifs.sort(Comparator.comparing(Notification::getCreateDate).reversed());

            notifs.removeIf(notif -> !(notif.getUserTo().getUsername().equals(username) || notif.getUserFrom().getUsername().equals(username)));

            return notifs;
        } catch (Exception e){
            LOG.warn("Notifications for user" + user.getUsername() + " to user " + username + " not found");
        }
        return new ArrayList<>();
    }

    public List<Notification> getPresentNotificationsForUser(Principal principal){
        User user = getUserByPrincipal(principal);

        try {
            LOG.debug("Find present notifications for user "+ user.getUsername());

            List<Notification> notificatins = notificationRepository.findAllByUserToOrderByCreateDate(user);
            notificatins.addAll(notificationRepository.findAllByUserFromOrderByCreateDate(user));
            notificatins.sort(Comparator.comparing(Notification::getCreateDate).reversed());

            List<Notification> presentNotif = new ArrayList<>();
            List<String> usernames = new ArrayList<>();
            for (Notification notif : notificatins) {
                if (notif.getUserTo() != null
                        && !usernames.contains(notif.getUserTo().getUsername())
                        && !notif.getUserTo().getUsername().equals(user.getUsername())
                ) {
                    usernames.add(notif.getUserTo().getUsername());
                    if (!presentNotif.contains(notif)) {
                        presentNotif.add(notif);
                    }
                }
                if (notif.getUserFrom() != null
                        && !usernames.contains(notif.getUserFrom().getUsername())
                        && !notif.getUserFrom().getUsername().equals(user.getUsername())
                ) {
                    usernames.add(notif.getUserFrom().getUsername());
                    if (!presentNotif.contains(notif)) {
                        presentNotif.add(notif);
                    }
                }
            }

            return presentNotif;
        }  catch (Exception e){
            LOG.warn("Present notifications for user" + user.getUsername() + " not found");
        }
        return new ArrayList<>();
    }

    public Notification createNotification(NotificationDTO notificationDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        LOG.debug("Create new notification for "+ user.getUsername());
        if (!notificationDTO.getUsernameTo().equals(user.getUsername())) {
            Notification notification = new Notification();

            notification.setUserFrom(user);
            User userTo = userRepository.findUserByUsername(notificationDTO.getUsernameTo())
                    .orElseThrow(() -> new UsernameNotFoundException("User-destination not found with username" + notificationDTO.getUsernameTo()));
            notification.setUserTo(userTo);
            notification.setTitle(notificationDTO.getTitle());
            notification.setMessage(notificationDTO.getMessage());
            notification.setHasRead(false);

            LOG.info("Created new notification from " + user.getEmail() + " for user: {}", userTo.getEmail());
            return notificationRepository.save(notification);
        } else {
            LOG.error("Error! You can`t send message for yourself! {}", user.getUsername());
            throw new NotificationCantBeSendForYourself("Ошибка! Нельзя отправлять сообщения самому себе! " + user.getUsername());
        }
    }

    public Notification readNotification(Principal principal, Long notificationId){
        User user = getUserByPrincipal(principal);

        try {
            LOG.debug("Find notification with id: " + notificationId + " and user: " + user.getUsername());
            Notification notification = notificationRepository.findNotificationByIdAndUserTo(notificationId, user)
                    .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + notificationId));

            notification.setHasRead(true);
            LOG.debug("Notification with id: " + notificationId + " has read");
            return notificationRepository.save(notification);
        } catch (NotificationNotFoundException e){
            LOG.error(e.getMessage());
        }
        return null;
    }


    public Notification updateNotification(NotificationDTO notificationDTO, Principal principal, Long notificationId){
        User user = getUserByPrincipal(principal);

        try {
            LOG.debug("Find notification with id: " + notificationId);
            Notification notification = notificationRepository.findNotificationByIdAndUserTo(notificationId, user)
                    .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + notificationId));

            notification.setMessage(notificationDTO.getMessage());
            notification.setTitle(notification.getTitle());
            LOG.info("Update notification with id: " + notificationId);
            return notificationRepository.save(notification);
        } catch (NotificationNotFoundException e){
            LOG.error(e.getMessage());
        }
        return null;
    }

    public void deleteNotification(Long notificationId){
        try {
            LOG.info("Delete notification with id: " + notificationId);
            Notification notification = notificationRepository.getById(notificationId);
            notificationRepository.delete(notification);
        } catch (Exception e){
            LOG.error("ERROR! Notification with id: " + notificationId  + " can`t be deleted");
        }
    }


    public User getUserByPrincipal(Principal principal){
        try {
            String username = principal.getName();
            return userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        } catch (Exception e){
            LOG.error("User not found by principal");
        }
        return null;
    }
}
