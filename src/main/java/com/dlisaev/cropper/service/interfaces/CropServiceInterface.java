package com.dlisaev.cropper.service.interfaces;

import com.dlisaev.cropper.entity.Crop;
import java.util.List;

public interface CropServiceInterface {

    public List<Crop> getAllCrops();

    public Crop createCrop(String nameCrop);

    public void deletCrop(Long cropId);
}
