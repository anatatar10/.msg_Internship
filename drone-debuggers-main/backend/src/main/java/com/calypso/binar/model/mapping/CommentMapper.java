package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.CommentDTO;
import com.calypso.binar.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CaseMapper.class, UserMapper.class})
public interface CommentMapper {

    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "commentText", target = "comment")
    CommentDTO toCommentDTO(Comment comment);


}
