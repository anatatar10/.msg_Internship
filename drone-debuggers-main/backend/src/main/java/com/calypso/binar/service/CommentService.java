package com.calypso.binar.service;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.Comment;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.CommentDTO;
import com.calypso.binar.repository.CommentRepository;
import com.calypso.binar.service.casedetails.CaseService;
import com.calypso.binar.service.exception.CaseNotFoundException;
import com.calypso.binar.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CaseService caseService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public List<CommentDTO> getAllCommentsForCase(String caseId){
        Optional<List<Comment>> comments = commentRepository.findAllByCaseId(caseId);

        if(comments.isEmpty()){
            comments=Optional.of(List.of());
        }

        return mapCommentsToDTO(comments.get());
    }

    private List<CommentDTO> mapCommentsToDTO(List<Comment> comments) {

        return comments.stream()
                .map(comment -> new CommentDTO(
                comment.getCommentId(),
                comment.getUser().getEmail(),
                comment.getUser().getRole().getRoleName(),
                comment.getCommentText(),
                comment.getTimestamp()
        )).collect(Collectors.toList());
    }

    public Comment addComment(CommentDTO commentDTO, String systemCaseId) throws CaseNotFoundException, UserNotFoundException {
        Case compensationCase = this.caseService.getCaseBySystemCaseId(systemCaseId);
        Optional<User> user = this.userService.getUserByEmail(commentDTO.getUserEmail());

        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        Comment comment = new Comment(compensationCase,user.get(), commentDTO.getComment(), commentDTO.getTimestamp());

        this.commentRepository.save(comment);
        return comment;
    }
}
