package com.dlisaev.cropper.payload.request;

import com.dlisaev.cropper.annotations.PasswordMatches;
import com.dlisaev.cropper.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignUpRequest {
    @Email(message = "Email имеет неверный формат")
    @NotBlank(message = "Поле Email обязательно")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Пожалуйста, введите имя")
    private String firstname;

    @NotEmpty(message = "Пожалуйста, введите фамилию")
    private String lastname;

    @NotEmpty(message = "Пожалуйста, введите логин")
    private String username;

    @NotEmpty(message = "Пожалуйста, введите местоположение")
    private String location;

    @NotEmpty(message = "Пожалуйста, введите пароль")
    @Size(min = 5, max = 50)
    private String password;

    @NotEmpty(message = "Пожалуйста, введите пароль еще раз")
    @Size(min = 5, max = 50)
    private String confirmPassword;
}