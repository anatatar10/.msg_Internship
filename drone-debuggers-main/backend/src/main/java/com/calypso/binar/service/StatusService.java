package com.calypso.binar.service;

import com.calypso.binar.model.Status;
import com.calypso.binar.repository.StatusRepository;
import com.calypso.binar.service.exception.StatusNonExistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;

    public Status getStatusByStatusName(String statusName) throws StatusNonExistent {
        Optional<Status> foundStatus =statusRepository.findByStatusName(statusName);
        if (foundStatus.isEmpty()) {
            throw new StatusNonExistent(statusName);
        }
        return foundStatus.get();
    }
}
