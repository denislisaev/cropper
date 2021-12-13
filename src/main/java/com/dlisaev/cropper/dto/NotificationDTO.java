package com.dlisaev.cropper.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class NotificationDTO {
    @NotEmpty
    private String usernameTo;

    private String usernameFrom;
    @NotEmpty
    private String title;
    @NotEmpty
    private String message;
}
