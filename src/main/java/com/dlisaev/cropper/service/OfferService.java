package com.dlisaev.cropper.service;

import com.dlisaev.cropper.dto.OfferDTO;
import com.dlisaev.cropper.dto.UserDTO;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.exceptions.CropNotFoundException;
import com.dlisaev.cropper.exceptions.OfferNotFoundException;
import com.dlisaev.cropper.repository.CropRepository;
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
public class OfferService {
    public static final Logger LOG = LoggerFactory.getLogger(OfferService.class);

    private final CropRepository cropRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(CropRepository cropRepository, UserRepository userRepository, OfferRepository offerRepository) {
        this.cropRepository = cropRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
    }

    public List<Offer> getAllOffers(){
        return offerRepository.findAllByOrderByCreateDate();
    }

    public List<Offer> getAllSellOffers(){
        return offerRepository.findAllByTypeOfferOrderByCreateDate(false);
    }

    public List<Offer> getAllBuyOffers(){
        return offerRepository.findAllByTypeOfferOrderByCreateDate(true);
    }

    public Offer getOfferById(Long offerId, Principal principal){
        User user = getUserByPrincipal(principal);
        return offerRepository.findOfferByIdAndUser(offerId, user)
                .orElseThrow(()-> new OfferNotFoundException("Offer not found for username: " + user.getEmail()));
    }

    public List<Offer> getAllOffersForUser(Principal principal){
        User user = getUserByPrincipal(principal);
        return offerRepository.findAllByUserOrderByCreateDate(user);
    }

    public Offer createPost(OfferDTO offerDto, Principal principal){
        User user = getUserByPrincipal(principal);
        Offer offer = new Offer();

        offer.setUser(user);
        offer.setCrop(cropRepository.findCropByName(offerDto.getCrop())
                .orElseThrow(() -> new CropNotFoundException("Crop not found with name: " + offerDto.getCrop())));
        offer.setTypeOffer(offerDto.getTypeOffer());
        offer.setVolume(offerDto.getVolume());
        offer.setPricePerTon(offerDto.getPricePerTon());
        offer.setInfo(offerDto.getInfo());

        LOG.info("Create new offer for user: {}", user.getEmail());
        return offerRepository.save(offer);
    }

    public Offer updateOffer(OfferDTO offerDTO, Long offerId, Principal principal){
        User user = getUserByPrincipal(principal);
        Offer offer = offerRepository.findOfferByIdAndUser(offerId, user)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with id: " + offerId));
        offer.setTypeOffer(offerDTO.getTypeOffer());
        offer.setInfo(offerDTO.getInfo());
        offer.setCrop(cropRepository.findCropByName(offerDTO.getCrop())
                .orElseThrow(() -> new CropNotFoundException("Crop not found with name: " + offerDTO.getCrop())));
        offer.setVolume(offerDTO.getVolume());
        offer.setPricePerTon(offerDTO.getPricePerTon());

        return offerRepository.save(offer);
    }

    public void deleteOffer(Long offerId, Principal principal){
        Offer offer = getOfferById(offerId, principal);
        offerRepository.delete(offer);
    }


    public User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
