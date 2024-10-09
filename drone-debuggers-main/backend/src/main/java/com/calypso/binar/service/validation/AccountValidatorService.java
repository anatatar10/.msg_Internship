package com.calypso.binar.service.validation;

import com.calypso.binar.model.User;
import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.service.exception.AccountLockedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountValidatorService {

    @Autowired
    private  UserRepository userRepository;

    //tests if the account is locked
    public  void isAccountLocked(String email) throws AccountLockedException {

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent() && user.get().isAccountBlocked()){

            throw new AccountLockedException();

        }

    }

    public void failedLoginAttempt(String email) {

        Optional<User> userFromRepo = userRepository.findByEmail(email);

        if (userFromRepo.isPresent()) {

            User user = userFromRepo.get();

            if (user.getNumberOfFailedAttempts() == 5) {
                user.setAccountBlocked(true);
                userRepository.save(user);

            } else {
                user.setNumberOfFailedAttempts(user.getNumberOfFailedAttempts() + 1);
                userRepository.save(user);
            }

    }


    }

    //if the login is successful, reset the user failed attempts to 0
    public  void onLoginSuccessResetFailCount(String email){
        Optional<User> userFromRepo = userRepository.findByEmail(email);
        if(userFromRepo.isPresent()) {
            User user = userFromRepo.get();
            user.setNumberOfFailedAttempts(0);
            userRepository.save(user);
        }
    }
}
