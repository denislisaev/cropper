package com.dlisaev.cropper.controllers;

import com.dlisaev.cropper.dto.UserDTO;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.entity.enums.ERole;
import com.dlisaev.cropper.facade.UserFacade;
import com.dlisaev.cropper.payload.response.MessageResponse;
import com.dlisaev.cropper.service.NotificationService;
import com.dlisaev.cropper.service.OfferService;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    private NotificationService notificationService;
    private OfferService offerService;
    private UserFacade userFacade;
    private UserService userService;
    private ResponseErrorValidator responseErrorValidator;

    @Autowired
    public UserController(UserFacade userFacade, UserService userService, ResponseErrorValidator responseErrorValidator, OfferService offerService, NotificationService notificationService) {
        this.userFacade = userFacade;
        this.userService = userService;
        this.responseErrorValidator = responseErrorValidator;
        this.offerService = offerService;
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser (Principal principal){
        User user = userService.getUserByPrincipal(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/role")
    public ResponseEntity<Set<ERole>> getRoleOfCurrentUser (Principal principal){
        User user = userService.getUserByPrincipal(principal);
        return new ResponseEntity<>(user.getRoles(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUserProfiles(Principal principal){
        if (this.userService.getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)){
            List<UserDTO> users = userService.getUsers()
                    .stream()
                    .map(userFacade::userToUserDTO)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Недостаточно прав!"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId){
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return  listErrors;

        User user = userService.updateUser(userDTO, principal);
        UserDTO userUpdated = userFacade.userToUserDTO(user);

        return  new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{username}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String username, Principal principal){
        if (this.userService.getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)) {
            if (!userService.getUserByPrincipal(principal).getUsername().equals(username)) {

                List<Offer> offers = this.offerService.getAllOffersForUsername(username);
                List<Notification> notifications = this.notificationService.getAllNotificationsForUserByUsername(username);
                List<Notification> notificationsFrom = this.notificationService.getAllNotificationsForUserFromByUsername(username);

                for (Offer offer : offers) {
                    this.offerService.deleteOfferAdmin(offer.getId());
                }

                for (Notification notification : notifications) {
                    this.notificationService.deleteNotification(notification.getId());
                }

                for (Notification notification : notificationsFrom) {
                    this.notificationService.deleteNotification(notification.getId());
                }

                this.userService.deleteUserByUsername(username);

                return new ResponseEntity<>(new MessageResponse("Пользователь " + username + " удален"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Нельзя удалить самого себя!"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Недостаточно прав!"), HttpStatus.BAD_REQUEST);
        }
    }

}
