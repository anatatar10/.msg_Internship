package com.calypso.binar.controller;

import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.ColleagueCreateDTO;
import com.calypso.binar.model.dto.UserListDTO;
import com.calypso.binar.service.EmailSenderService;
import com.calypso.binar.service.UserService;
import com.calypso.binar.service.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private EmailSenderService emailSenderService;

    @Test
    public void findAllPassengerColleague_returnsUserListWithStatusOk_whenUsersArePresent() {
        // Arrange
        UserListDTO user1 = new UserListDTO("John", "Doe", "john.doe@example.com", "Admin", 5L);
        UserListDTO user2 = new UserListDTO("Jane", "Smith", "jane.smith@example.com", "User", 2L);
        List<UserListDTO> users = Arrays.asList(user1, user2);

        when(userService.getPassengerColleagueListWithCaseCounts()).thenReturn(users);

        // Act
        ResponseEntity<List<UserListDTO>> response = userController.findAllPassengerColleague();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
    }

    @Test
    public void findAllUser_returnsUserListWithStatusOk_whenUsersArePresent() {
        // Arrange
        User user1 = new User();
        user1.setEmail("john.doe@example.com");
        User user2 = new User();
        user2.setEmail("jane.smith@example.com");
        List<User> users = Arrays.asList(user1, user2);

        when(userService.findAllUser()).thenReturn(users);

        // Act
        ResponseEntity<List<User>> response = userController.findAllUser();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
    }

    @Test
    public void createColleague_returnsSuccessMessage_whenColleagueIsCreated() throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
        // Given
        ColleagueCreateDTO colleagueDTO = new ColleagueCreateDTO();
        colleagueDTO.setEmail("colleague@example.com");
        colleagueDTO.setPassword("securePassword");

        User user = new User();
        user.setEmail("colleague@example.com");

        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("user", user);
        serviceResponse.put("initialPassword", "initialPassword123");

        when(userService.createColleague(colleagueDTO)).thenReturn(serviceResponse);

        // When
        ResponseEntity<String> response = userController.createColleague(colleagueDTO);

        // Then
        assertThat(response.getBody()).isEqualTo("{\"response\":\"Passenger created successfully\"}");

        // Verify emailSenderService.sendInitialPassword was called with correct parameters
        verify(emailSenderService).sendInitialPassword("colleague@example.com", "Initial Password", "initialPassword123");
    }
    @Test
    public void createColleague_throwsNonUniqueEmailException_whenEmailAlreadyExists() throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
        // Given
        ColleagueCreateDTO colleagueDTO = new ColleagueCreateDTO();
        colleagueDTO.setEmail("existing@example.com");

        when(userService.createColleague(colleagueDTO)).thenThrow(new NonUniqueEmailException("ExistingEmail"));

        // When & Then
        NonUniqueEmailException thrown = assertThrows(
                NonUniqueEmailException.class,
                () -> userController.createColleague(colleagueDTO)
        );

        assertThat(thrown.getMessage()).isEqualTo("ExistingEmail is already in use!");
    }

    @Test
    public void createColleague_throwsInvalidEmailException_whenEmailIsInvalid() throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
        // Given
        ColleagueCreateDTO colleagueDTO = new ColleagueCreateDTO();
        colleagueDTO.setEmail("invalid-email");

        when(userService.createColleague(colleagueDTO)).thenThrow(new InvalidEmailException("InvalidEmail"));

        // When & Then
        InvalidEmailException thrown = assertThrows(
                InvalidEmailException.class,
                () -> userController.createColleague(colleagueDTO)
        );

        assertThat(thrown.getMessage()).isEqualTo("InvalidEmail is not a valid email address!");
    }

    @Test
    public void createColleague_throwsRoleDoesNotExistException_whenRoleIsNotFound() throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
        // Given
        ColleagueCreateDTO colleagueDTO = new ColleagueCreateDTO();
        colleagueDTO.setEmail("colleague@example.com");

        when(userService.createColleague(colleagueDTO)).thenThrow(new RoleDoesNotExistException("NonExistentRole"));

        // When & Then
        RoleDoesNotExistException thrown = assertThrows(
                RoleDoesNotExistException.class,
                () -> userController.createColleague(colleagueDTO)
        );

        assertThat(thrown.getMessage()).isEqualTo("NonExistentRole does not exist");
    }

    @Test
    public void handleServiceException_returnsErrorResponse_whenServiceExceptionIsThrown() {
        // Given
        ServiceException serviceException = new ServiceException("Service error", "123");

        // When
        ResponseEntity<Map<String, String>> response = userController.handleAuthExceptions(serviceException);

        // Then
        assertEquals(418, response.getStatusCodeValue());
        assertThat(response.getBody()).containsEntry("error_code", "123");
    }

    @Test
    public void deleteUser_deletesUserSuccessfully_whenUserExists() throws UserNotFoundException, UserNotFoundException {
        // Given
        User user = new User();
        user.setEmail("a@a.a");
        user.setUserId(1);

        // Mock the service to perform the delete operation without throwing an exception
        doNothing().when(userService).deleteUser(1);
        when(userService.findUserByEmail(user.getEmail())).thenReturn(user);

        // When
        ResponseEntity<String> response = userController.deleteUser(user.getEmail());

        // Then
        assertThat(response.getBody()).isEqualTo("{\"response\":\"User deleted successfully\"}");
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    public void deleteUser_throwsUserNotFoundException_whenUserDoesNotExist() throws UserNotFoundException {
        // Given
        User user = new User();
        user.setEmail("a@a.a");
        user.setUserId(1);

        // Mock the service to throw UserNotFoundException when deleteUser is called
        doThrow(new UserNotFoundException()).when(userService).findUserByEmail(user.getEmail());

        // When & Then
        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> userController.deleteUser(user.getEmail())
        );

        // Assert exception message or type
        assertThat(thrown).isInstanceOf(UserNotFoundException.class);

        // Verify the service method was called
        verify(userService, times(1)).findUserByEmail(user.getEmail());
    }


}
