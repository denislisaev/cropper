package com.dlisaev.cropper.facade;

import com.dlisaev.cropper.dto.UserDTO;
import com.dlisaev.cropper.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setLocation(user.getLocation());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreateDate(user.getCreateDate());

        return userDTO;
    }
}