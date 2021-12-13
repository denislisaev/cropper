package com.dlisaev.cropper.service;

import com.dlisaev.cropper.dto.NotificationDTO;
import com.dlisaev.cropper.entity.Crop;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.exceptions.CropAlreadyExist;
import com.dlisaev.cropper.exceptions.NotificationCantBeSendForYourself;
import com.dlisaev.cropper.exceptions.NotificationNotFoundException;
import com.dlisaev.cropper.repository.CropRepository;
import com.dlisaev.cropper.repository.NotificationRepository;
import com.dlisaev.cropper.repository.OfferRepository;
import com.dlisaev.cropper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class CropService {
    public static final Logger LOG = LoggerFactory.getLogger(CropService.class);

    private final CropRepository cropRepository;


    @Autowired
    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }


    public List<Crop> getAllCrops(){
        return cropRepository.findAll();
    }


    public Crop createCrop(String nameCrop){
        if (cropRepository.findCropByName(nameCrop).isEmpty()) {
            Crop crop = new Crop();
            crop.setName(nameCrop);

            LOG.info("Create new crop  {}", nameCrop);
            return cropRepository.save(crop);
        } else {
            LOG.error("Crop already exist! {}", nameCrop);
            throw new CropAlreadyExist("Crop already exist! " + nameCrop);
        }
    }


    public void deletCrop(Long cropId){
        Crop crop = cropRepository.getById(cropId);
        cropRepository.delete(crop);
    }

}
