package com.calypso.binar.service;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.Role;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.ColleagueCreateDTO;
import com.calypso.binar.model.dto.PassengerCreateDTO;
import com.calypso.binar.model.dto.UserListDTO;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.security.PasswordGenerator;
import com.calypso.binar.service.exception.InvalidEmailException;
import com.calypso.binar.service.exception.NonUniqueEmailException;
import com.calypso.binar.service.exception.RoleDoesNotExistException;
import com.calypso.binar.service.exception.UserNotFoundException;
import com.calypso.binar.service.validation.EmailValidatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private EmailValidatorService emailValidatorService;

    @InjectMocks
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordGenerator passwordGenerator;



    /**
     * Test to verify that a user is successfully created when all validations pass.
     * Ensures that the email is validated, password is hashed, and the user is saved to the repository.
     */
    @Test
    public void createUser_savesUserSuccessfully_whenValidationsPass() throws NonUniqueEmailException, InvalidEmailException {
        // Given
        User user = new User();
        String email = "test@test.com";
        user.setEmail(email);
        String originalPw = "password";
        user.setPassword(originalPw);
        Role role = new Role();
        user.setRole(role);

        // No try-catch needed, just let the exceptions propagate
        doNothing().when(emailValidatorService).completeEmailValidation(email);

        when(userRepository.save(user)).thenReturn(user);

        // When
        User savedUser = userService.createUser(user);

        // Then
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isNotEqualTo(originalPw); // password should be hashed
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test to verify that all users are correctly retrieved from the repository.
     * Ensures that the repository returns the expected list of users.
     */
    @Test
    public void findAllUser_returnsAllUsersSuccessfully_whenUsersExist() {
        // Given
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // When
        List<User> users = userService.findAllUser();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).containsExactly(user1, user2);
    }

    /**
     * Test to verify that a user is correctly retrieved by ID from the repository.
     * Ensures that the user is found and returned correctly.
     */
    @Test
    public void getUserById_returnsUserSuccessfully_whenUserExists() {
        // Given
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // When
        Optional<User> retrievedUser = userService.getUserById(1);

        // Then
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualTo(user);
    }

    /**
     * Test to verify that a user is successfully deleted by ID.
     * Ensures that the user is deleted from the repository when the method is called.
     */
    @Test
    public void deleteUserById_deletesUserSuccessfully_whenUserExists() {
        // Given
        doNothing().when(userRepository).deleteById(1);

        // When
        userService.deleteUserById(1);

        // Then
        verify(userRepository, times(1)).deleteById(1);
    }

    /**
     * Test to verify that a user is successfully loaded by username.
     * Ensures that the user details are correctly retrieved by email.
     */
    @Test
    public void loadUserByUsername_returnsUserDetailsSuccessfully_whenUserExists() {
        // Given
        User user = new User();
        String username = "test@example.com";
        user.setEmail(username);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = userService.loadUserByUsername("test@example.com");

        // Then
        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
    }

    /**
     * Test to verify that an exception is thrown when trying to load a user by username that does not exist.
     * Ensures that a `UsernameNotFoundException` is thrown when the user is not found by email.
     */
    @Test
    public void loadUserByUsername_throwsUsernameNotFoundException_whenUserDoesNotExist() {
        // Given
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class, () -> userService.loadUserByUsername(email)
        );
        assertThat(thrown).isInstanceOf(UsernameNotFoundException.class);
    }

    /**
     * Test to verify that a user is successfully retrieved by email.
     * Ensures that the user is found and returned correctly.
     */
    @Test
    public void testGetUserByEmail_UserExists() {
        // Arrange
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> actualUser = userService.getUserByEmail(email);

        // Assert
        assertTrue(actualUser.isPresent(), "User should be found");
        assertEquals(expectedUser, actualUser.get(), "The user returned should match the expected user");
    }

    /**
     * Test to verify that a user is not found when trying to retrieve a user by email that does not exist.
     * Ensures that the user is not found when the email does not exist in the repository.
     */
    @Test
    public void testGetUserByEmail_UserDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> actualUser = userService.getUserByEmail(email);

        // Assert
        assertTrue(actualUser.isEmpty(), "User should not be found");
    }

    /**
     * Test to verify that all users with assigned cases are correctly retrieved from the repository.
     * Ensures that the repository returns the expected list of users with assigned cases.
     */
    @Test
    void getUserListWithCaseCounts_returnsCorrectCounts() {
        // Arrange
        Role colleagueRole = new Role();
        colleagueRole.setRoleName("Colleague");

        Role passengerRole = new Role();
        passengerRole.setRoleName("Passenger");

        User user1 = new User();
        user1.setUserId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setRole(colleagueRole);

        User user2 = new User();
        user2.setUserId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setRole(passengerRole);

        Case case1 = new Case();
        case1.setCaseId(101);
        case1.setColleague(user1); // Assign user1 as colleague

        Case case2 = new Case();
        case2.setCaseId(102);
        case2.setColleague(user1); // Assign user1 as colleague

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(caseRepository.findAll()).thenReturn(Arrays.asList(case1, case2));

        // Act
        List<UserListDTO> result = userService.getUserListWithCaseCounts();

        // Assert
        assertEquals(2, result.size());

        UserListDTO dto1 = result.get(0);
        assertEquals("John", dto1.getFirstName());
        assertEquals("Doe", dto1.getLastName());
        assertEquals("john.doe@example.com", dto1.getEmail());
        assertEquals("Colleague", dto1.getRoleName());
        assertEquals(2, dto1.getNoAssignedCases());

        UserListDTO dto2 = result.get(1);
        assertEquals("Jane", dto2.getFirstName());
        assertEquals("Smith", dto2.getLastName());
        assertEquals("jane.smith@example.com", dto2.getEmail());
        assertEquals("Passenger", dto2.getRoleName());
        assertEquals(0, dto2.getNoAssignedCases());

        verify(userRepository, times(1)).findAll();
        verify(caseRepository, times(1)).findAll();
    }

    @Test
    public void createPassenger_createsPassengerSuccessfully_whenRoleExist() throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
        // Given
        PassengerCreateDTO passengerCreateDTO = new PassengerCreateDTO();
        passengerCreateDTO.setEmail("test@test.com");
        passengerCreateDTO.setFirstName("John");
        passengerCreateDTO.setLastName("Doe");

        User passengerUser = new User();
        passengerUser.setEmail(passengerCreateDTO.getEmail());
        passengerUser.setFirstName(passengerCreateDTO.getFirstName());
        passengerUser.setLastName(passengerCreateDTO.getLastName());
        Role role = new Role();
        passengerUser.setRole(role);
        passengerUser.setFirstLogin(true);
        passengerUser.setAccountBlocked(false);
        passengerUser.setNumberOfFailedAttempts(0);

        String initialPassword = "initialPassword";
        passengerUser.setPassword(initialPassword);

        when(roleService.findRoleByRoleName("Passenger")).thenReturn(role);
        when(passwordGenerator.generateRandomPassword()).thenReturn(initialPassword);
        when(userRepository.save(ArgumentMatchers.eq(passengerUser))).thenReturn(passengerUser);

        // When
        Map<String, Object> result = userService.createPassenger(passengerCreateDTO);

        ArgumentCaptor<User> userCaptor = forClass(User.class);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("initialPassword")).isEqualTo(initialPassword);
        assertThat(result.get("user")).isEqualTo(passengerUser);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertTrue(passwordEncoder.matches(initialPassword, capturedUser.getPassword()));

        assertThat(capturedUser.getEmail()).isEqualTo(passengerUser.getEmail());
        assertThat(capturedUser.getFirstName()).isEqualTo(passengerUser.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(passengerUser.getLastName());
        assertThat(capturedUser.getRole()).isEqualTo(passengerUser.getRole());
        assertThat(capturedUser.isFirstLogin()).isEqualTo(passengerUser.isFirstLogin());
        assertThat(capturedUser.isAccountBlocked()).isEqualTo(passengerUser.isAccountBlocked());
        assertThat(capturedUser.getNumberOfFailedAttempts()).isEqualTo(passengerUser.getNumberOfFailedAttempts());
    }

    @Test
    public void createPassenger_createsPassengerSuccessfully_whenRoleDoesntExist() throws RoleDoesNotExistException {
        // Given
        PassengerCreateDTO passengerCreateDTO = new PassengerCreateDTO();
        passengerCreateDTO.setEmail("test@test.com");
        passengerCreateDTO.setFirstName("John");
        passengerCreateDTO.setLastName("Doe");

        User passengerUser = new User();
        passengerUser.setEmail(passengerCreateDTO.getEmail());
        passengerUser.setFirstName(passengerCreateDTO.getFirstName());
        passengerUser.setLastName(passengerCreateDTO.getLastName());
        Role role = new Role();
        passengerUser.setRole(role);
        passengerUser.setFirstLogin(true);
        passengerUser.setAccountBlocked(false);
        passengerUser.setNumberOfFailedAttempts(0);

        String initialPassword = "initialPassword";
        passengerUser.setPassword(initialPassword);

        when(roleService.findRoleByRoleName("Passenger")).thenThrow(RoleDoesNotExistException.class);

        // When & Then
        RoleDoesNotExistException thrown = assertThrows(
                RoleDoesNotExistException.class, () -> userService.createPassenger(passengerCreateDTO)
        );

        assertThat(thrown).isInstanceOf(RoleDoesNotExistException.class);
    }


    @Test
    public void getPassengerColleagueListWithCaseCounts_returnsCorrectCounts_whenUsersAndCasesExist() {
        // Given
        Role colleagueRole = new Role();
        colleagueRole.setRoleName("Colleague");

        Role passengerRole = new Role();
        passengerRole.setRoleName("Passenger");

        User user1 = new User();
        user1.setUserId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setRole(colleagueRole);

        User user2 = new User();
        user2.setUserId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setRole(passengerRole);

        Case case1 = new Case();
        case1.setCaseId(101);
        case1.setColleague(user1); // Assign user1 as colleague

        Case case2 = new Case();
        case2.setCaseId(102);
        case2.setColleague(user1); // Assign user1 as colleague

        when(userRepository.findAllByRoleNames(Arrays.asList("Passenger", "Colleague")))
                .thenReturn(Arrays.asList(user1, user2));
        when(caseRepository.findAll()).thenReturn(Arrays.asList(case1, case2));

        // When
        List<UserListDTO> result = userService.getPassengerColleagueListWithCaseCounts();

        // Then
        assertEquals(2, result.size());

        UserListDTO dto1 = result.get(0);
        assertEquals("John", dto1.getFirstName());
        assertEquals("Doe", dto1.getLastName());
        assertEquals("john.doe@example.com", dto1.getEmail());
        assertEquals("Colleague", dto1.getRoleName());
        assertEquals(2, dto1.getNoAssignedCases());

        UserListDTO dto2 = result.get(1);
        assertEquals("Jane", dto2.getFirstName());
        assertEquals("Smith", dto2.getLastName());
        assertEquals("jane.smith@example.com", dto2.getEmail());
        assertEquals("Passenger", dto2.getRoleName());
        assertEquals(0, dto2.getNoAssignedCases());

        verify(userRepository, times(1)).findAllByRoleNames(Arrays.asList("Passenger", "Colleague"));
        verify(caseRepository, times(1)).findAll();
    }

    @Test
    public void getPassengerColleagueListWithCaseCounts_returnsEmptyList_whenNoUsersOrCasesExist() {
        // Given
        when(userRepository.findAllByRoleNames(Arrays.asList("Passenger", "Colleague"))).thenReturn(Collections.emptyList());
        when(caseRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserListDTO> result = userService.getPassengerColleagueListWithCaseCounts();

        // Then
        assertTrue(result.isEmpty(), "The result should be an empty list when no users or cases exist");

        verify(userRepository, times(1)).findAllByRoleNames(Arrays.asList("Passenger", "Colleague"));
        verify(caseRepository, times(1)).findAll();
    }


    @Test
    public void createColleague_createsColleagueSuccessfully_whenRoleExists() throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
        // Given
        ColleagueCreateDTO colleagueCreateDTO = new ColleagueCreateDTO();
        colleagueCreateDTO.setEmail("colleague@test.com");
        colleagueCreateDTO.setFirstName("Alice");
        colleagueCreateDTO.setLastName("Johnson");
        colleagueCreateDTO.setPassword("password123");

        Role role = new Role();
        role.setRoleName("Colleague");

        User createdUser = new User();
        createdUser.setEmail(colleagueCreateDTO.getEmail());
        createdUser.setFirstName(colleagueCreateDTO.getFirstName());
        createdUser.setLastName(colleagueCreateDTO.getLastName());
        createdUser.setRole(role);
        createdUser.setFirstLogin(true);
        createdUser.setAccountBlocked(false);
        createdUser.setNumberOfFailedAttempts(0);
        createdUser.setPassword(colleagueCreateDTO.getPassword());

        // Mock roleService response
        when(roleService.findRoleByRoleName("Colleague")).thenReturn(role);

        // Mock createUser method
        when(userRepository.save(any(User.class))).thenReturn(createdUser);

        // When
        Map<String, Object> result = userService.createColleague(colleagueCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals(colleagueCreateDTO.getPassword(), result.get("initialPassword"));
        assertEquals(createdUser, result.get("user"));

        verify(roleService, times(1)).findRoleByRoleName("Colleague");
        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    public void createColleague_throwsRoleDoesNotExistException_whenRoleDoesNotExist() throws RoleDoesNotExistException {
        // Given
        ColleagueCreateDTO colleagueCreateDTO = new ColleagueCreateDTO();
        colleagueCreateDTO.setEmail("colleague@test.com");
        colleagueCreateDTO.setFirstName("Alice");
        colleagueCreateDTO.setLastName("Johnson");
        colleagueCreateDTO.setPassword("password123");

        when(roleService.findRoleByRoleName("Colleague")).thenThrow(RoleDoesNotExistException.class);

        // When & Then
        RoleDoesNotExistException thrown = assertThrows(
                RoleDoesNotExistException.class, () -> userService.createColleague(colleagueCreateDTO)
        );

        assertThat(thrown).isInstanceOf(RoleDoesNotExistException.class);
        verify(roleService, times(1)).findRoleByRoleName("Colleague");
    }


    @Test
    public void deleteUser_deletesUserSuccessfully_whenUserExists() throws UserNotFoundException {
        // Given
        int userId = 1;

        // Mock the repository to return true when deleteByUserId is called
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        // When
        userService.deleteUser(userId);

        ArgumentCaptor<Integer> userCaptor = ArgumentCaptor.forClass(Integer.class);

        // Then
        verify(userRepository, times(1)).deleteByUserId(userCaptor.capture());

        Integer capturedUserId = userCaptor.getValue();
        assertThat(capturedUserId).isEqualTo(userId);
    }

    @Test
    public void deleteUser_throwsUserNotFoundException_whenUserDoesNotExist() {
        // Given
        int userId = 2;

        // Mock the repository to return false when deleteByUserId is called
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // When & Then
        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class, () -> userService.deleteUser(userId)
        );

        // Verify that the exception is of the correct type
        assertThat(thrown).isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(userId);
    }



}
