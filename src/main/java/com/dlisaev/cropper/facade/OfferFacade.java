package com.dlisaev.cropper.facade;

import com.dlisaev.cropper.dto.OfferDTO;
import com.dlisaev.cropper.dto.UserDTO;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OfferFacade {
    @Autowired
    CropRepository cropRepository;

    public OfferDTO offerToOfferDTO(Offer offer){
        OfferDTO offerDTO = new OfferDTO();

        offerDTO.setTypeOffer(offer.getTypeOffer());
        offerDTO.setCrop(offer.getCrop().getName());
        offerDTO.setInfo(offer.getInfo());
        offerDTO.setVolume(offer.getVolume());
        offerDTO.setUsername(offer.getUser().getUsername());
        offerDTO.setPricePerTon(offer.getPricePerTon());

        return offerDTO;
    }
}