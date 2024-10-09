package com.calypso.binar.controller;

import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.ColleagueCreateDTO;
import com.calypso.binar.model.dto.PassengerCreateDTO;
import com.calypso.binar.model.dto.UserListDTO;
import com.calypso.binar.service.EmailSenderService;
import com.calypso.binar.service.UserService;
import com.calypso.binar.service.exception.*;
import com.calypso.binar.service.exception.InvalidEmailException;
import com.calypso.binar.service.exception.NonUniqueEmailException;
import com.calypso.binar.service.exception.RoleDoesNotExistException;
import com.calypso.binar.service.exception.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = {"/api/user", "/api/user/"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    /**
     * This method returns a list of all users with assigned cases.
     * The method is accessible only to users with the role "System Admin".
     */
    @GetMapping("/show-list")
    @Secured({"System Admin"})
    public ResponseEntity<List<UserListDTO>> findAllUsersForAdmin() {
        List<UserListDTO> userListDTOS = userService.getUserListWithCaseCounts();
        return ResponseEntity.ok(userListDTOS);
    }

    @GetMapping("/show-all-passenger-colleague")
    @Secured({"System Admin"})
    public ResponseEntity<List<UserListDTO>> findAllPassengerColleague() {
        List<UserListDTO> userListDTOS = userService.getPassengerColleagueListWithCaseCounts();
        return ResponseEntity.ok(userListDTOS);
    }

    @GetMapping
    @Secured({"Colleague"})
    public ResponseEntity<List<User>> findAllUser() {
        return ResponseEntity.ok(userService.findAllUser());
    }


    /**
     * Creates a new passenger, sends an initial password via email, and returns a success message.
     *
     * @param passenger PassengerCreateDTO containing passenger details.
     * @return ResponseEntity<String> with a success message.
     * @throws NonUniqueEmailException if the email already exists.
     * @throws InvalidEmailException if the email is invalid.
     * @throws RoleDoesNotExistException if the specified role is not found.
     */
    @PostMapping("/create-passenger")
    public ResponseEntity<String> createPassenger(@Valid @RequestBody PassengerCreateDTO passenger) throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
        Map<String,Object> response=userService.createPassenger(passenger);

        User returnedUser = (User) response.get("user");
        String initialPassword = (String) response.get("initialPassword");

        emailSenderService.sendInitialPassword(returnedUser.getEmail(), "Initial Password", initialPassword);
        String jsonString = "{\"response\":\"Passenger created successfully\"}";
        return ResponseEntity.ok(jsonString);
    }

    /**
     * Creates a new colleague, sends the given password via email, and returns a success message.
     *
     * @param colleague PassengerCreateDTO containing passenger details.
     * @return ResponseEntity<String> with a success message.
     * @throws NonUniqueEmailException if the email already exists.
     * @throws InvalidEmailException if the email is invalid.
     * @throws RoleDoesNotExistException if the specified role is not found.
     */
    @PostMapping("/create-colleague")
    @Secured({"System Admin"})
    public ResponseEntity<String> createColleague(@Valid @RequestBody ColleagueCreateDTO colleague) throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
        Map<String,Object> response=userService.createColleague(colleague);

        User returnedUser = (User) response.get("user");
        String initialPassword = (String) response.get("initialPassword");

        emailSenderService.sendInitialPassword(returnedUser.getEmail(), "Initial Password", initialPassword);

        String jsonString = "{\"response\":\"Passenger created successfully\"}";
        return ResponseEntity.ok(jsonString);
    }

    /**
     * This method deletes a user by their ID.
     * @param email the email of the user to delete
     * @return a ResponseEntity with a success message
     * @throws UserNotFoundException
     */
    @DeleteMapping("/delete-user/{email}")
    @Secured({"System Admin"})
    public ResponseEntity<String> deleteUser(@PathVariable String email) throws UserNotFoundException {
        User user = userService.findUserByEmail(email);
        userService.deleteUser(user.getUserId());

        return ResponseEntity.ok("{\"response\":\"User deleted successfully\"}");
    }

    /**
     * This method handles exceptions thrown by the service layer.
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, String>> handleAuthExceptions(ServiceException e) {
        // Create the error response as a map
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error_code", e.getExceptionID());

        // Return the error response with the appropriate HTTP status
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(418));
    }

}
