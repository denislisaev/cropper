package com.dlisaev.cropper.repository;

import com.dlisaev.cropper.entity.Crop;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findCropById(Long id);
    Optional<Crop> findCropByName(String name);
}
