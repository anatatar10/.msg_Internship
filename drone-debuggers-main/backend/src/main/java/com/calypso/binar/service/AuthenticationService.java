package com.calypso.binar.service;

import com.calypso.binar.SignInRequest;
import com.calypso.binar.SignInResponse;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.PasswordResetDTO;
import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.security.JwtService;
import com.calypso.binar.service.exception.AccountLockedException;
import com.calypso.binar.service.exception.FailedAuthenticationException;
import com.calypso.binar.service.exception.UserNotFoundException;
import com.calypso.binar.service.validation.AccountValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    //Returns a valid JWT Token in SignInResponse if the username and password are correct
    public SignInResponse signIn(SignInRequest signInRequest) throws FailedAuthenticationException, AccountLockedException {

        //testing if the account is locked
        accountValidatorService.isAccountLocked(signInRequest.getUsername());

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();

            String jwt = jwtService.generateJwtToken(user);

            accountValidatorService.onLoginSuccessResetFailCount(signInRequest.getUsername());

            return new SignInResponse(jwt, user.getRoles(), user.isFirstLogin());
        }
        catch (Exception e){
            accountValidatorService.failedLoginAttempt(signInRequest.getUsername());
            throw new FailedAuthenticationException();
        }

    }

    public User passwordReset(PasswordResetDTO userToResetPassword) throws UserNotFoundException {

        Optional<User> ifExist = userService.getUserByEmail(userToResetPassword.getEmail());

        if(ifExist.isPresent()) {
            User user;
            user = ifExist.get();
            user.setPassword(new BCryptPasswordEncoder().encode(userToResetPassword.getPassword()));
            user.setFirstLogin(false);
            userRepository.save(user);
            return user;
        }
        else {
            throw new UserNotFoundException();
        }

    }

    public User getActiveUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void logOut(String authToken){
        jwtService.logOut(authToken);
    }
}
