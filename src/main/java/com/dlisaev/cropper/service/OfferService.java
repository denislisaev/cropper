package com.dlisaev.cropper.service;

import com.dlisaev.cropper.dto.OfferDTO;
import com.dlisaev.cropper.entity.Crop;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.entity.User;
import com.dlisaev.cropper.exceptions.CropNotFoundException;
import com.dlisaev.cropper.exceptions.OfferNotFoundException;
import com.dlisaev.cropper.repository.CropRepository;
import com.dlisaev.cropper.repository.OfferRepository;
import com.dlisaev.cropper.repository.UserRepository;
import com.dlisaev.cropper.service.interfaces.OfferServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class OfferService implements OfferServiceInterface {
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
        LOG.debug("Get all offers");
        return offerRepository.findAllByOrderByCreateDate();
    }

    public List<Offer> getAllSellOffers(){
        LOG.debug("Get all sell offers");
        return offerRepository.findAllByTypeOfferOrderByCreateDate(false);
    }

    public List<Offer> getAllBuyOffers(){

        LOG.debug("Get all buy offers");
        return offerRepository.findAllByTypeOfferOrderByCreateDate(true);
    }

    public Offer getOfferById(Long offerId, Principal principal){
        User user = getUserByPrincipal(principal);
        try {
            LOG.debug("Get offer by id: " + offerId + " for user: " + user.getUsername());
            return offerRepository.findOfferByIdAndUser(offerId, user)
                    .orElseThrow(() -> new OfferNotFoundException("Offer not found for username: " + user.getUsername()));
        } catch (OfferNotFoundException e){
            LOG.error(e.getMessage());
        }
        return null;
    }

    public Offer getOfferByIdAdmin(Long offerId){
        try{
            LOG.debug("(ADMIN) Get offer by id: " + offerId);
            return offerRepository.findOfferById(offerId)
                    .orElseThrow(()-> new OfferNotFoundException("Offer not found for id: " + offerId));
        } catch (OfferNotFoundException e){
            LOG.error(e.getMessage());
        }
        return null;
    }

    public List<Offer> getAllOffersForUser(Principal principal){
        User user = getUserByPrincipal(principal);
        try{
            LOG.debug("Get all offers for user: " + user.getUsername());
            return offerRepository.findAllByUserOrderByCreateDate(user);
        } catch (Exception e){
            LOG.error("Offers for user: " + user.getUsername() + " not found");
        }
        return null;
    }

    public List<Offer> getAllOffersForUsername(String username){
        try {
            LOG.debug("Get all offers for username: " +  username);
            User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
            return offerRepository.findAllByUserOrderByCreateDate(user);
        } catch (UsernameNotFoundException e){
            LOG.error(e.getMessage());
        } catch (Exception e){
            LOG.error("Offers for username: " + username + " not found");
        }
        return null;
    }

    public Offer createOffer(OfferDTO offerDto, Principal principal){
        User user = getUserByPrincipal(principal);
        Offer offer = new Offer();

        try {
            offer.setUser(user);
            offer.setCrop(cropRepository.findCropByName(offerDto.getCrop())
                    .orElseThrow(() -> new CropNotFoundException("Crop not found with name: " + offerDto.getCrop())));
            offer.setTypeOffer(offerDto.getTypeOffer());
            offer.setVolume(offerDto.getVolume());
            offer.setPricePerTon(offerDto.getPricePerTon());
            offer.setInfo(offerDto.getInfo());

            LOG.info("Create new offer for user: {}", user.getEmail());
            return offerRepository.save(offer);
        } catch (CropNotFoundException e){
            LOG.error(e.getMessage());
        } catch (Exception e){
            LOG.error("Offer has not created");
        }
        return null;
    }

    public Offer updateOffer(OfferDTO offerDTO, Long offerId, Principal principal){
        try {
            User user = getUserByPrincipal(principal);
            Offer offer = offerRepository.findOfferByIdAndUser(offerId, user)
                    .orElseThrow(() -> new OfferNotFoundException("Offer not found with id: " + offerId));
            offer.setTypeOffer(offerDTO.getTypeOffer());
            offer.setInfo(offerDTO.getInfo());
            offer.setCrop(cropRepository.findCropByName(offerDTO.getCrop())
                    .orElseThrow(() -> new CropNotFoundException("Crop not found with name: " + offerDTO.getCrop())));
            offer.setVolume(offerDTO.getVolume());
            offer.setPricePerTon(offerDTO.getPricePerTon());

            LOG.info("Update offer with ID: {}",offerId);

            return offerRepository.save(offer);
        } catch (CropNotFoundException | OfferNotFoundException e) {
            LOG.error(e.getMessage());
        } catch (Exception e){
            LOG.error("Offer has not updated");
        }
        return null;
    }

    public void deleteOffer(Long offerId, Principal principal){
        LOG.info("Delete offer with ID: {}",offerId);
        Offer offer = getOfferById(offerId, principal);
        try {
            offerRepository.delete(offer);
        } catch (Exception e){
            LOG.error("Offer with id {} has not deleted", offerId);
        }
    }


    public User getUserByPrincipal(Principal principal){
        try {
            String username = principal.getName();
            return userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        } catch (Exception e){
            LOG.error("User not found by principal");
        }
        return null;
    }

    public void deleteOfferAdmin(long offerId) {
        LOG.info("(ADMIN) Delete offer with ID: {}",offerId);
        Offer offer = getOfferByIdAdmin(offerId);
        try {
            offerRepository.delete(offer);
        } catch (Exception e){
            LOG.error("Offer with id {} has not deleted", offerId);
        }
    }
}
