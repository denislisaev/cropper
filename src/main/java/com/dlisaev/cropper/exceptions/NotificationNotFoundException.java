package com.dlisaev.cropper.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotificationNotFoundException extends RuntimeException{
    public NotificationNotFoundException(String message) {super(message);}
}
