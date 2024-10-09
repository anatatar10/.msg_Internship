package com.calypso.binar.controller;

import com.calypso.binar.model.Comment;
import com.calypso.binar.model.dto.CommentDTO;
import com.calypso.binar.service.CommentService;
import com.calypso.binar.service.exception.CaseNotFoundException;
import com.calypso.binar.service.exception.ServiceException;
import com.calypso.binar.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = {"/api/comment", "/api/comment/"})
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<CommentDTO>> getCommentsForCase(@PathVariable String caseId){
        List<CommentDTO> commentDTOS = commentService.getAllCommentsForCase(caseId);

        return ResponseEntity.ok(commentDTOS);
    }

    @PostMapping("/add/{systemCaseId}")
    public ResponseEntity<Comment> addCommentForCase(@PathVariable String systemCaseId, @RequestBody CommentDTO commentDTO ) throws UserNotFoundException, CaseNotFoundException {
        Comment comment = commentService.addComment(commentDTO, systemCaseId);

        return ResponseEntity.ok(comment);
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
