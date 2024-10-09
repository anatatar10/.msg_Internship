package com.calypso.binar.repository;

import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.UserListDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.role.roleName = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    void deleteByUserId(Integer userId);

    @Query("SELECT new com.calypso.binar.model.dto.UserListDTO(" +
            "u.firstName, u.lastName, " +
            "u.email, " +
            "r.roleName, " +
            "COUNT(DISTINCT c.caseId)) " +
            "FROM User u " +
            "LEFT JOIN u.role r " +
            "LEFT JOIN Case c ON u.userId = c.passenger.userId OR u.userId = c.colleague.userId " +
            "GROUP BY u.userId, u.firstName, u.lastName, u.email, r.roleName")
    List<UserListDTO> findAllUsersForAdminList();

    @Query("SELECT u FROM User u WHERE u.role.roleName IN (:roles)")
    List<User> findAllByRoleNames(@Param("roles") List<String> roles);
}
