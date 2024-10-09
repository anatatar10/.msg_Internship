package com.calypso.binar.service;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.Comment;
import com.calypso.binar.model.Role;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.CommentDTO;
import com.calypso.binar.repository.CommentRepository;
import com.calypso.binar.service.exception.CommentsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    public void getAllCommentsForCase_returnsComments_whenCommentsExist() throws CommentsNotFoundException {
        // Given
        int caseId = 1;
        Case caseEntity = new Case();
        caseEntity.setCaseId(1);

        // Create User object for comments
        User user = new User();
        user.setUserId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(new Role(1,"Passenger"));

        // Create fully populated Comment objects
        Comment comment1 = new Comment();
        comment1.setCommentId(101);
        comment1.setCaseEntity(caseEntity);
        comment1.setUser(user);
        comment1.setCommentText("Test comment 1");
        comment1.setTimestamp(Timestamp.from(Instant.now()));

        Comment comment2 = new Comment();
        comment2.setCommentId(102);
        comment2.setCaseEntity(caseEntity);
        comment2.setUser(user);
        comment2.setCommentText("Test comment 2");
        comment2.setTimestamp(Timestamp.from(Instant.now()));

        // Create the list of comments
        List<Comment> comments = Arrays.asList(comment1, comment2);

        // Mock the repository call
        when(commentRepository.findAllByCaseId(String.valueOf(caseId))).thenReturn(Optional.of(comments));

        // When
        List<CommentDTO> result = commentService.getAllCommentsForCase(String.valueOf(caseId));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getComment()).isEqualTo("Test comment 1");
        assertThat(result.get(1).getComment()).isEqualTo("Test comment 2");

        // Additional assertions on other fields
        assertThat(result.get(0).getUserEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.get(1).getUserEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void getAllCommentsForCase_returnsEmptyList_whenNoCommentsFound() {
        // Given
        String caseId = "case123";
        // Mocking the repository to return an empty Optional list
        when(commentRepository.findAllByCaseId(caseId)).thenReturn(Optional.of(Collections.emptyList()));

        // When
        List<CommentDTO> result = commentService.getAllCommentsForCase(caseId);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void getAllCommentsForCase_returnsEmptyList_whenOptionalIsEmpty() {
        String caseId = "case123";
        when(commentRepository.findAllByCaseId(caseId)).thenReturn(Optional.empty());
        List<CommentDTO> result = commentService.getAllCommentsForCase(caseId);
        assertEquals(0, result.size());
    }

}
