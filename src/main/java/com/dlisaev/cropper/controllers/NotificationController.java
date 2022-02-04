package com.dlisaev.cropper.controllers;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.dto.NotificationInputDTO;
import com.dlisaev.cropper.dto.PresentNotificationDTO;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.facade.NotificationFacade;
import com.dlisaev.cropper.payload.response.MessageResponse;
import com.dlisaev.cropper.service.NotificationService;
import com.dlisaev.cropper.service.UserService;
import com.dlisaev.cropper.validators.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin
public class NotificationController {
    private NotificationFacade notificationFacade;
    private NotificationService notificationService;
    private ResponseErrorValidator responseErrorValidator;
    private UserService userService;

    @Autowired
    public NotificationController(NotificationFacade NotificationFacade, NotificationService NotificationService, ResponseErrorValidator responseErrorValidator, UserService userService) {
        this.notificationFacade = NotificationFacade;
        this.notificationService = NotificationService;
        this.responseErrorValidator = responseErrorValidator;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNotification (@Valid @RequestBody NotificationDTO notificationDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (userService.getUserByPrincipal(principal).getUsername().equals(notificationDTO.getUsernameTo()))
            return new ResponseEntity<>(new MessageResponse("Нельзя отправлять сообщения самому себе!"), HttpStatus.BAD_REQUEST);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        Notification notification = notificationService.createNotification(notificationDTO, principal);
        NotificationDTO  notificationCreated = notificationFacade.notificationToNotificationDTO(notification);

        return new ResponseEntity<>(notificationCreated, HttpStatus.OK);
    }


    @GetMapping("/notifications/{username}")
    public ResponseEntity<List<NotificationInputDTO>> getAllNotificationsForUser(@PathVariable("username") String username, Principal principal){
        List<NotificationInputDTO> notificationDTOList = notificationService.getAllNotificationsForUserToUser(principal, username)
                .stream()
                .map(notificationFacade::notificationToNotificationInputDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(notificationDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/notifications/present")
    public ResponseEntity<List<PresentNotificationDTO>> getAllNotificationsUsernamesForUser(Principal principal){
        List<PresentNotificationDTO> presentNotificationDTOList = notificationService.getPresentNotificationsForUser(principal)
                .stream()
                .map(notificationFacade::notificationToPresentNotificationDTO)
                .collect(Collectors.toList());

        for (PresentNotificationDTO dto : presentNotificationDTOList){
            if (!dto.getUsernameFrom().equals(userService.getUserByPrincipal(principal).getUsername())){
                dto.setUsername(dto.getUsernameFrom());
            } else {
                if (!dto.getUsernameTo().equals(userService.getUserByPrincipal(principal).getUsername())){
                    dto.setUsername(dto.getUsernameTo());
                }
            }
        }


        return new ResponseEntity<>(presentNotificationDTOList, HttpStatus.OK);
    }

    @PostMapping("/{notificationId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("notificationId") String notificationId, Principal principal){
        notificationService.deleteNotification(Long.parseLong(notificationId));
        return new ResponseEntity<>(new MessageResponse("The notification " + notificationId + " was deleted"), HttpStatus.OK);
    }
}
