package com.dlisaev.cropper.controllers;

import com.dlisaev.cropper.entity.Crop;
import com.dlisaev.cropper.entity.enums.ERole;
import com.dlisaev.cropper.payload.response.MessageResponse;
import com.dlisaev.cropper.service.CropService;
import com.dlisaev.cropper.service.UserService;
import com.dlisaev.cropper.validators.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/crop")
@CrossOrigin
public class CropController {
    private final UserService userService;
    private CropService cropService;
    private ResponseErrorValidator responseErrorValidator;

    @Autowired
    public CropController(CropService CropService, UserService userService, ResponseErrorValidator responseErrorValidator) {
        this.cropService = CropService;
        this.responseErrorValidator = responseErrorValidator;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createCrop (@Valid @RequestBody String cropName, BindingResult bindingResult, Principal principal){
        if (this.userService.getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)){
            System.out.println("Добавление культуры");
            ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
            if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

            Crop crop = cropService.createCrop(cropName);

            return new ResponseEntity<>(crop, HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Недостаточно прав!"), HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/crops")
    public ResponseEntity<List<Crop>> getAllCrops(Principal principal){
        List<Crop> cropList = new ArrayList<>(cropService.getAllCrops());

        return  new ResponseEntity<>(cropList, HttpStatus.OK);
    }

    @DeleteMapping("/{cropId}/delete")
    public ResponseEntity<MessageResponse> deleteCrop(@PathVariable("cropId") String cropId, Principal principal){
        if (this.userService.getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)){
            cropService.deletCrop(Long.parseLong(cropId));
            return new ResponseEntity<>(new MessageResponse("The crop " + cropId + " was deleted"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Недостаточно прав!"), HttpStatus.BAD_REQUEST);
    }

}
