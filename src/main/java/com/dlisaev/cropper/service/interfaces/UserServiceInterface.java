package com.dlisaev.cropper.service.interfaces;

import com.dlisaev.cropper.dto.UserDTO;
import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.entity.enums.ERole;
import com.dlisaev.cropper.exceptions.UserAlreadyException;
import com.dlisaev.cropper.payload.request.SignUpRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public interface UserServiceInterface {
    public User createUser(SignUpRequest userIn);

    public User updateUser(UserDTO userDTO, Principal principal);

    public User getUserByPrincipal(Principal principal);

    public User getCurrentUser(Principal principal);

    public User getUserById(Long userId);
}
