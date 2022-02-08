package com.dlisaev.cropper.repository;

import com.dlisaev.cropper.entity.Crop;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findCropByName(String name);
    Optional<Crop> findCropById(Long Id);

}
