package com.dlisaev.cropper.service;

import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.exceptions.UserIdNotFoundException;
import com.dlisaev.cropper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigUserDetailsService implements UserDetailsService {
    public static final Logger LOG = LoggerFactory.getLogger(CropService.class);
    private final UserRepository userRepository;

    public ConfigUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Ищем пользователя в бд
    public UserDetails loadUserByUsername (String username){
        try {
            User user = userRepository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            LOG.debug("Load user with username:  {}", username);
            return initUser(user);
        } catch (UsernameNotFoundException e){
            LOG.error(e.getMessage());
        }
        return null;
    }

    //Конвертация в Spring Sequrity
    public static User initUser(User user){
        List<GrantedAuthority> authorityList = user.getRoles().stream()
                .map(eRole -> new SimpleGrantedAuthority(eRole.name()))
                .collect(Collectors.toList());

        //Наделяем пользователя полномочиями
        return new User(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorityList);
    }

    public User loadUserById(Long id){
        try {
            LOG.debug("Load user with id:  {}", id);
            return userRepository.findUserById(id).orElseThrow(()-> new UserIdNotFoundException("User not found with id: " + id));
        } catch (UserIdNotFoundException e){
            LOG.error(e.getMessage());
            return null;
        }
    }
}
