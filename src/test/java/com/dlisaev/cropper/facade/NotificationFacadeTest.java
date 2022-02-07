package com.dlisaev.cropper.facade;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.dto.NotificationInputDTO;
import com.dlisaev.cropper.dto.PresentNotificationDTO;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.User;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class NotificationFacadeTest {

    User userTo = new User();
    User userFrom = new User();
    private Notification notification = new Notification();
    private NotificationFacade notificationFacade = new NotificationFacade();


    @Test
    void notificationToNotificationDTO() {
        this.userTo.setUsername("usernameTo");

        this.userFrom.setUsername("usernameFrom");

        this.notification.setId(1L);
        this.notification.setUserTo(this.userTo);
        this.notification.setUserFrom(this.userFrom);
        this.notification.setTitle("title");
        this.notification.setMessage("message");
        this.notification.setHasRead(true);

        NotificationDTO ntfDTO = new NotificationDTO();
        ntfDTO.setUsernameTo(this.userTo.getUsername());
        ntfDTO.setTitle("title");
        ntfDTO.setMessage("message");

        Assertions.assertEquals(this.notificationFacade.notificationToNotificationDTO(notification), ntfDTO);
    }

    @Test
    void notificationToPresentNotificationDTO() {
        this.userTo.setUsername("usernameTo");

        this.userFrom.setUsername("usernameFrom");

        this.notification.setId(1L);
        this.notification.setUserTo(this.userTo);
        this.notification.setUserFrom(this.userFrom);
        this.notification.setTitle("title");
        this.notification.setMessage("message");
        this.notification.setHasRead(true);

        PresentNotificationDTO ntfDTO = new PresentNotificationDTO();
        ntfDTO.setUsernameFrom(this.userFrom.getUsername());
        ntfDTO.setUsernameTo(this.userTo.getUsername());
        ntfDTO.setTitle("title");
        ntfDTO.setMessage("message");
        ntfDTO.setHasRead(notification.getHasRead());

        Assertions.assertEquals(this.notificationFacade.notificationToPresentNotificationDTO(notification), ntfDTO);
    }

    @Test
    void notificationToNotificationInputDTO() {
        this.userTo.setUsername("usernameTo");

        this.userFrom.setUsername("usernameFrom");

        this.notification.setId(1L);
        this.notification.setUserTo(this.userTo);
        this.notification.setUserFrom(this.userFrom);
        this.notification.setTitle("title");
        this.notification.setMessage("message");
        this.notification.setHasRead(true);
        this.notification.setCreateDate(LocalDateTime.now());

        NotificationInputDTO ntfDTO = new NotificationInputDTO();
        ntfDTO.setUsernameFrom(this.userFrom.getUsername());
        ntfDTO.setTitle("title");
        ntfDTO.setMessage("message");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ntfDTO.setCreateDate(notification.getCreateDate().format(formatter));

        Assertions.assertEquals(this.notificationFacade.notificationToNotificationInputDTO(notification), ntfDTO);
    }

    @After
    public void afterTest() {
        this.notification = null;
        this.userTo = null;
        this.userFrom = null;
    }

}