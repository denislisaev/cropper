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
    @Email(message = "It should be email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstname;

    @NotEmpty(message = "Please enter your last name")
    private String lastname;

    @NotEmpty(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "Please enter your location")
    private String location;

    @NotEmpty(message = "Please enter your password")
    @Size(min = 5, max = 50)
    private String password;

    @NotEmpty(message = "Please enter your password again")
    @Size(min = 5, max = 50)
    private String confirmPassword;
}