package com.dlisaev.cropper.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;

    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotEmpty
    private String location;

    private LocalDateTime createDate;
}

