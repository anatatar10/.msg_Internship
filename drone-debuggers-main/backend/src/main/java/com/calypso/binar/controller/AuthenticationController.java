package com.calypso.binar.controller;

import com.calypso.binar.SignInRequest;
import com.calypso.binar.SignInResponse;
import com.calypso.binar.SignOutRequest;
import com.calypso.binar.model.dto.PasswordResetDTO;
import com.calypso.binar.service.AuthenticationService;
import com.calypso.binar.service.exception.AccountLockedException;
import com.calypso.binar.service.exception.FailedAuthenticationException;
import com.calypso.binar.service.exception.ServiceException;
import com.calypso.binar.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, maxAge = 3600)
@RequestMapping(path = {"/api/auth", "/api/auth/"})
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(path = "login")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) throws AccountLockedException, FailedAuthenticationException {
        SignInResponse signInResponse = authenticationService.signIn(signInRequest);
        return ResponseEntity.ok(signInResponse);
    }

    @PostMapping(path = "logout")
    public ResponseEntity<String> signOut(@RequestBody SignOutRequest signInRequest){
        authenticationService.logOut(signInRequest.getToken());
        return ResponseEntity.ok("{\"response\": \"User signed out.\"}");
    }

    @PostMapping(path = "change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordResetDTO passwordResetDTO) throws UserNotFoundException {
        authenticationService.passwordReset(passwordResetDTO);
        return ResponseEntity.ok("{\"response\": \"Password changed successfully.\"}");
    }


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, String>> handleAuthExceptions(ServiceException e) {
        // Create the error response as a map
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error_code", e.getExceptionID());

        // Return the error response with the appropriate HTTP status
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(418));
    }
}
