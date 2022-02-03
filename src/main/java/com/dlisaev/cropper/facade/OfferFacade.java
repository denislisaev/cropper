package com.dlisaev.cropper.facade;

import com.dlisaev.cropper.dto.OfferDTO;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.repository.CropRepository;
import com.dlisaev.cropper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;


@Component
public class OfferFacade {
    @Autowired
    CropRepository cropRepository;
    @Autowired
    UserRepository userRepository;

    public OfferDTO offerToOfferDTO(Offer offer){
        OfferDTO offerDTO = new OfferDTO();

        offerDTO.setId(offer.getId());
        offerDTO.setTypeOffer(offer.getTypeOffer());
        offerDTO.setCrop(offer.getCrop().getName());
        offerDTO.setInfo(offer.getInfo());
        offerDTO.setVolume(offer.getVolume());
        offerDTO.setUsername(offer.getUser().getUsername());
        offerDTO.setPricePerTon(offer.getPricePerTon());
        offerDTO.setLocation(userRepository.findUserByUsername(offerDTO.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Пользователь не найден")).getLocation());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = offer.getCreateDate().format(formatter);

        offerDTO.setDate(formattedDateTime);
        return offerDTO;
    }
}