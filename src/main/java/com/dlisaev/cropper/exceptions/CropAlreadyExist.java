package com.dlisaev.cropper.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CropAlreadyExist extends RuntimeException {
    public CropAlreadyExist(String s) {
        super(s);
    }
}
