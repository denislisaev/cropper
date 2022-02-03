package com.dlisaev.cropper.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PresentNotificationDTO {
    @NotEmpty
    private String usernameFrom;
    @NotEmpty
    private String usernameTo;
    @NotEmpty
    private String title;
    @NotEmpty
    private String message;
    @NotNull
    private boolean hasRead;

    private String username;
}
