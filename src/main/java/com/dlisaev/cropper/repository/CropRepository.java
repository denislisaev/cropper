package com.dlisaev.cropper.repository;

import com.dlisaev.cropper.entity.Crop;
import com.dlisaev.cropper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findUserById(Long id);
    Optional<Crop> findUserByName(String name);
}
