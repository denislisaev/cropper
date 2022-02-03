package com.dlisaev.cropper.repository;

import com.dlisaev.cropper.entity.Crop;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Optional<Offer> findOfferById(Long id);
    Optional<Offer> findOfferByIdAndUser(Long id, User user);
    List<Offer> findAllByOrderByCreateDate();
    List<Offer> findAllByUserOrderByCreateDate(User user);
    List<Offer> findAllByUserAndTypeOfferOrderByCreateDate(User user, Boolean typeOffer);
    List<Offer> findAllByCropOrderByCreateDate(Crop crop);
    List<Offer> findAllByCropAndTypeOfferOrderByCreateDate(Crop crop, Boolean typeOffer);
    List<Offer> findAllByTypeOfferOrderByCreateDate(Boolean typeOffer);
    List<Offer> findAllByTypeOfferOrderByCreateDateDesc(Boolean typeOffer);
}
