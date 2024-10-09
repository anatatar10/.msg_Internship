package com.calypso.binar.repository;

import com.calypso.binar.model.Comment;
import com.calypso.binar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c " +
            "JOIN Case cc ON c.caseEntity.caseId = cc.caseId " +
            "JOIN User u ON c.user.userId = u.userId " +
            "WHERE cc.caseId = :caseId")
    Optional<List<Comment>> findAllByDBCaseId(@Param("caseId") int caseId);

    Optional<List<Comment>> findCommentsByUser(User user);
    @Query("SELECT c FROM Comment c " +
            "JOIN Case cc ON c.caseEntity.caseId = cc.caseId " +
            "JOIN User u ON c.user.userId = u.userId " +
            "WHERE cc.systemCaseId = :systemCaseId")
    Optional<List<Comment>> findAllByCaseId(@Param("systemCaseId") String systemCaseId);
}
