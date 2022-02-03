package com.dlisaev.cropper.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NotificationInputDTO {
    @NotEmpty
    private String usernameFrom;
    @NotEmpty
    private String title;
    @NotEmpty
    private String message;
    @NotNull
    private String createDate;
}
