package com.dlisaev.cropper.controllers;

import com.dlisaev.cropper.dto.OfferDTO;
import com.dlisaev.cropper.dto.OfferListDTO;
import com.dlisaev.cropper.entity.Offer;
import com.dlisaev.cropper.entity.enums.ERole;
import com.dlisaev.cropper.facade.OfferFacade;
import com.dlisaev.cropper.payload.response.MessageResponse;
import com.dlisaev.cropper.service.OfferService;
import com.dlisaev.cropper.service.UserService;
import com.dlisaev.cropper.validators.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offer")
@CrossOrigin
public class OfferController {
    private OfferFacade offerFacade;
    private OfferService offerService;
    private ResponseErrorValidator responseErrorValidator;
    private UserService userService;

    @Autowired
    public OfferController(OfferFacade OfferFacade, OfferService OfferService, ResponseErrorValidator responseErrorValidator, UserService userService) {
        this.offerFacade = OfferFacade;
        this.offerService = OfferService;
        this.responseErrorValidator = responseErrorValidator;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createOffer (@Valid @RequestBody OfferDTO offerDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;
        try {
            Offer offer = offerService.createOffer(offerDTO, principal);
            OfferDTO offerCreated = offerFacade.offerToOfferDTO(offer);
            return new ResponseEntity<>(offerCreated, HttpStatus.OK);
        } catch(Exception e){
            if (e.getMessage().contains("Value is negative!")){
                if (e.getMessage().contains("volume")){
                    return new ResponseEntity<>(new MessageResponse("Значение объема не может быть отрицательным!"), HttpStatus.BAD_REQUEST);
                } else if (e.getMessage().contains("pricePerTon")){
                    return new ResponseEntity<>(new MessageResponse("Значение цены не может быть отрицательным!"), HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(new MessageResponse("Ошибка при создании оффера!"), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<OfferDTO>> getAllOffers(){
        List<OfferDTO> offerDTOList = offerService.getAllOffers()
                .stream()
                .map(offerFacade::offerToOfferDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(offerDTOList, HttpStatus.OK);
    }

    @GetMapping("/all/sell")
    public ResponseEntity<List<OfferDTO>> getAllSellOffers(){
        List<OfferDTO> offerDTOList = offerService.getAllSellOffers()
                .stream()
                .map(offerFacade::offerToOfferDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(offerDTOList, HttpStatus.OK);
    }

    @GetMapping("/all/buy")
    public ResponseEntity<List<OfferDTO>> getAllBuyOffers(){
        List<OfferDTO> offerDTOList = offerService.getAllBuyOffers()
                .stream()
                .map(offerFacade::offerToOfferDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(offerDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/offers")
    public ResponseEntity<List<OfferDTO>> getAllOffersForUser(Principal principal){
        List<OfferDTO> offerDTOList = offerService.getAllOffersForUser(principal)
                .stream()
                .map(offerFacade::offerToOfferDTO)
                .collect(Collectors.toList());

        return  new ResponseEntity<>(offerDTOList, HttpStatus.OK);
    }

    @PostMapping("/{offerId}/delete/admin")
    public ResponseEntity<MessageResponse> deleteOfferAdmin(@PathVariable("offerId") String offerId, Principal principal){
        if (this.userService.getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)){
            offerService.deleteOfferAdmin(Long.parseLong(offerId));
            return new ResponseEntity<>(new MessageResponse("The offer " + offerId + " was deleted by admin"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Недостаточно прав!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{offerId}/delete")
    public ResponseEntity<MessageResponse> deleteOffer(@PathVariable("offerId") String offerId, Principal principal){
        offerService.deleteOffer(Long.parseLong(offerId), principal);
        return new ResponseEntity<>(new MessageResponse("The offer " + offerId + " was deleted"), HttpStatus.OK);
    }

    @PostMapping("/{offerId}/update")
    public ResponseEntity<Object> updateOffer (@Valid @RequestBody OfferDTO offerDTO, @PathVariable("offerId") String offerId, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        try {
            Offer offer = offerService.updateOffer(offerDTO, Long.valueOf(offerId), principal);
            OfferDTO  offerUpdated= offerFacade.offerToOfferDTO(offer);
            return new ResponseEntity<>(offerUpdated, HttpStatus.OK);
        } catch(Exception e){
            if (e.getMessage().contains("Value is negative!")){
                if (e.getMessage().contains("volume")){
                    return new ResponseEntity<>(new MessageResponse("Значение объема не может быть отрицательным!"), HttpStatus.BAD_REQUEST);
                } else if (e.getMessage().contains("pricePerTon")){
                    return new ResponseEntity<>(new MessageResponse("Значение цены не может быть отрицательным!"), HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(new MessageResponse("Ошибка при обновлении оффера!"), HttpStatus.BAD_REQUEST);
        }
    }
}
