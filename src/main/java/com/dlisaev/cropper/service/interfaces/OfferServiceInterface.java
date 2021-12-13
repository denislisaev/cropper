package com.dlisaev.cropper.service.interfaces;

import com.dlisaev.cropper.dto.OfferDTO;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.entity.User;

import java.security.Principal;
import java.util.List;

public interface OfferServiceInterface {
    public List<Offer> getAllOffers();

    public List<Offer> getAllSellOffers();

    public List<Offer> getAllBuyOffers();

    public Offer getOfferById(Long offerId, Principal principal);

    public List<Offer> getAllOffersForUser(Principal principal);

    public Offer createOffer(OfferDTO offerDto, Principal principal);

    public Offer updateOffer(OfferDTO offerDTO, Long offerId, Principal principal);

    public void deleteOffer(Long offerId, Principal principal);

    public User getUserByPrincipal(Principal principal);

}
