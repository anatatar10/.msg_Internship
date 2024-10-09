package com.calypso.binar.service;

import com.calypso.binar.model.PassengerDetails;
import com.calypso.binar.model.dto.PassengerDetailsDTO;
import com.calypso.binar.model.mapping.PassengerDetailsMapper;
import com.calypso.binar.repository.PassengerDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassengerDetailsService {

    @Autowired
    private PassengerDetailsRepository passengerDetailsRepository;

    @Autowired
    private PassengerDetailsMapper passengerDetailsMapper;

    public PassengerDetailsDTO getPassengerDetailsById(int id) {
        PassengerDetails passengerDetails = passengerDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PassengerDetails not found"));
        return passengerDetailsMapper.toPassengerDetailsDTO(passengerDetails);
    }

    public PassengerDetailsDTO getPassengerDetailsForCase(String caseId){
        Optional<PassengerDetails> passengerDetails = passengerDetailsRepository.findByCaseId(caseId);

        if (passengerDetails.isEmpty()) {
            return null;
        }
        return passengerDetailsMapper.toPassengerDetailsDTO(passengerDetails.get());
    }

}
