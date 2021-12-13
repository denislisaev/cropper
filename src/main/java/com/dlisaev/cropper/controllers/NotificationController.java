package com.dlisaev.cropper.controllers;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.facade.NotificationFacade;
import com.dlisaev.cropper.payload.response.MessageResponse;
import com.dlisaev.cropper.service.NotificationService;
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

    @Autowired
    public NotificationController(NotificationFacade NotificationFacade, NotificationService NotificationService, ResponseErrorValidator responseErrorValidator) {
        this.notificationFacade = NotificationFacade;
        this.notificationService = NotificationService;
        this.responseErrorValidator = responseErrorValidator;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNotification (@Valid @RequestBody NotificationDTO notificationDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        Notification notification = notificationService.createNotification(notificationDTO, principal);
        NotificationDTO  notificationCreated = notificationFacade.notificationToNotificationDTO(notification);

        return new ResponseEntity<>(notificationCreated, HttpStatus.OK);
    }


    @GetMapping("/user/notifications")
    public ResponseEntity<List<NotificationDTO>> getAllNotificationsForUser(Principal principal){
        List<NotificationDTO> notificationDTOList = notificationService.getAllNotificationsForUser(principal)
                .stream()
                .map(notificationFacade::notificationToNotificationDTO)
                .collect(Collectors.toList());

        return  new ResponseEntity<>(notificationDTOList, HttpStatus.OK);
    }

    @PostMapping("/{notificationId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("notificationId") String notificationId, Principal principal){
        notificationService.deleteNotification(Long.parseLong(notificationId));
        return new ResponseEntity<>(new MessageResponse("The notification " + notificationId + " was deleted"), HttpStatus.OK);
    }
}
