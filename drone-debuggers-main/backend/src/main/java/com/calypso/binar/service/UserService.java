package com.calypso.binar.service;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.Role;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.ColleagueCreateDTO;
import com.calypso.binar.model.*;
import com.calypso.binar.model.dto.PassengerCreateDTO;
import com.calypso.binar.model.dto.UserListDTO;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.repository.CommentRepository;
import com.calypso.binar.repository.StatusRepository;
import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.security.PasswordGenerator;
import com.calypso.binar.service.exception.InvalidEmailException;
import com.calypso.binar.service.exception.NonUniqueEmailException;
import com.calypso.binar.service.exception.RoleDoesNotExistException;
import com.calypso.binar.service.exception.UserNotFoundException;
import com.calypso.binar.service.validation.EmailValidatorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  private static final String COLLEAGUE = "Colleague";
  private static final String PASSENGER = "Passenger";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CaseRepository caseRepository;

  @Autowired
  private EmailValidatorService emailValidatorService;


  @Autowired
  private RoleService roleService;

  @Autowired
  private PasswordGenerator passwordGenerator;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private StatusRepository statusRepository;

    /**
     * Loads a user by their email, primarily used for authentication.
     *
     * @param email the email of the user to be loaded
     * @return the user details associated with the provided email
     * @throws UsernameNotFoundException if the user is not found with the given email
     */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));
  }

  /**
   *
   * @param passengerCreateDTO
   * @return
   * @throws NonUniqueEmailException
   * @throws InvalidEmailException
   * @throws RoleDoesNotExistException
   */
  @Transactional
  public Map<String, Object> createPassenger(PassengerCreateDTO passengerCreateDTO) throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
    return createUserWithRole(
            passengerCreateDTO.getEmail(),
            passengerCreateDTO.getFirstName(),
            passengerCreateDTO.getLastName(),
            PASSENGER,
            passwordGenerator.generateRandomPassword()
    );
  }

  @Transactional
  public Map<String, Object> createColleague(ColleagueCreateDTO colleagueCreateDTO) throws NonUniqueEmailException, InvalidEmailException, RoleDoesNotExistException {
    return createUserWithRole(
            colleagueCreateDTO.getEmail(),
            colleagueCreateDTO.getFirstName(),
            colleagueCreateDTO.getLastName(),
            COLLEAGUE,
            colleagueCreateDTO.getPassword()
    );
  }

  private Map<String, Object> createUserWithRole(String email, String firstName, String lastName, String roleName, String password) throws RoleDoesNotExistException, InvalidEmailException, NonUniqueEmailException {
    User user = new User();

    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);

    Role role = roleService.findRoleByRoleName(roleName);
    user.setRole(role);

    user.setFirstLogin(true);
    user.setAccountBlocked(false);
    user.setNumberOfFailedAttempts(0);

    user.setPassword(password);

    User createdUser = this.createUser(user);

    Map<String, Object> result = new HashMap<>();
    result.put("initialPassword", password);
    result.put("user", createdUser);

    return result;
  }



  /**
   * Creates a new user in the system after validating and encrypting their password.
   *
   * @param user the user entity to be created
   * @return the created user entity
   * @throws NonUniqueEmailException if the email is not unique
   * @throws InvalidEmailException   if the email is invalid
   */
  @Transactional
  public User createUser(User user) throws NonUniqueEmailException, InvalidEmailException {
    //check if email is unique and valid
    emailValidatorService.completeEmailValidation(user.getEmail());

    //hash password
    user.setPassword((new BCryptPasswordEncoder()).encode(user.getPassword()));

    return userRepository.save(user);
  }

  /**
   * Retrieves all users in the system.
   *
   * @return a list of all users
   */
  public List<User> findAllUser() {
    return userRepository.findAll();
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return an {@link Optional} containing the user if found, or empty if not
   */
  public Optional<User> getUserById(Integer id) {
    return userRepository.findById(id);
  }

  /**
   * Deletes a user by their ID.
   *
   * @param id the ID of the user to delete
   */
  public void deleteUserById(Integer id) {
    userRepository.deleteById(id);
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user to retrieve
   * @return an {@link Optional} containing the user if found, or empty if not
   */
  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Retrieves a list of all users with their assigned roles and the count of distinct cases assigned to them.
   *
   * @return a list of {@link UserListDTO} objects containing user information and their assigned case counts
   */

  public List<UserListDTO> getUserListWithCaseCounts() {
    List<User> users = userRepository.findAll();
    return getUserListDTOS(users);
  }

  public List<UserListDTO> getPassengerColleagueListWithCaseCounts() {
    ArrayList<String> rolesList = new ArrayList<>();
    rolesList.add(PASSENGER);
    rolesList.add(COLLEAGUE);
    List<User> users = userRepository.findAllByRoleNames(rolesList);
    return getUserListDTOS(users);
  }

  private List<UserListDTO> getUserListDTOS(List<User> users) {
    List<Case> cases = caseRepository.findAll();

    List<UserListDTO> userListDTOS = new ArrayList<>();

    for (User user : users) {
      int numberOfCases = 0;

      if (user.getRole().getRoleName().equals(COLLEAGUE)) {
        numberOfCases = (int) cases.stream()
                .filter(c -> c.getColleague() != null && c.getColleague().getUserId().equals(user.getUserId()))
                .count();
      }

      UserListDTO userListDto = new UserListDTO(
              user.getFirstName(),
              user.getLastName(),
              user.getEmail(),
              user.getRole().getRoleName(),
              (long) numberOfCases
      );

      userListDTOS.add(userListDto);
    }

    return userListDTOS;
  }


  /**
   * Deletes a user by their ID.
   * @param id the ID of the user to delete
   * @throws UserNotFoundException
   */
  public void deleteUser(int id) throws UserNotFoundException {
    Optional<User> user = userRepository.findById(id);
    if(user.isEmpty()) {
      throw new UserNotFoundException();
    }

    // Use the COLLEAGUE constant instead of the hardcoded "Colleague" string
    if(user.get().getRole() != null && user.get().getRole().getRoleName().equals(COLLEAGUE)){
      Status newStatus = statusRepository.findByStatusName("NEW").get();
      Status assignedStatus = statusRepository.findByStatusName("ASSIGNED").get();
      User unknown = userRepository.findByEmail("unknown").get();

      Optional<List<Case>> cases = caseRepository.findCasesByColleague(user.get());
      cases.ifPresent(value -> value.forEach(c -> {
        c.setColleague(unknown);
        if(c.getStatus().getStatusName().equals(assignedStatus.getStatusName())){
          c.setStatus(newStatus);
        }
        caseRepository.save(c);
      }));

      Optional<List<Comment>> comments = commentRepository.findCommentsByUser(user.get());
      comments.ifPresent(value -> value.forEach(c -> {
        c.setUser(unknown);
        commentRepository.save(c);
      }));
    }

    userRepository.deleteByUserId(id);
  }


  public User findUserByEmail(String email) throws UserNotFoundException {
    Optional<User> user = userRepository.findByEmail(email);

    if(user.isEmpty()) {
      throw new UserNotFoundException();
    }
    return user.get();
  }
}
