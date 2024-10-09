package com.calypso.binar.controller;

import com.calypso.binar.model.dto.CommentDTO;
import com.calypso.binar.service.CommentService;
import com.calypso.binar.service.exception.CommentsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Test
    public void getCommentsForCase_returnsCommentDTOList_whenCommentsExist() throws CommentsNotFoundException {
        // Given
        int caseId = 1;

        CommentDTO comment1 = new CommentDTO();
        comment1.setComment("Test comment 1");
        comment1.setUserEmail("john.doe@example.com");

        CommentDTO comment2 = new CommentDTO();
        comment2.setComment("Test comment 2");
        comment2.setUserEmail("jane.doe@example.com");

        List<CommentDTO> commentDTOList = Arrays.asList(comment1, comment2);

        // Mocking the service to return a list of comments
        when(commentService.getAllCommentsForCase(String.valueOf(caseId))).thenReturn(commentDTOList);

        // When
        ResponseEntity<List<CommentDTO>> response = commentController.getCommentsForCase(String.valueOf(caseId));

        // Then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(0).getComment()).isEqualTo("Test comment 1");
        assertThat(response.getBody().get(1).getComment()).isEqualTo("Test comment 2");
        verify(commentService, Mockito.times(1)).getAllCommentsForCase(String.valueOf(caseId));
    }

}
