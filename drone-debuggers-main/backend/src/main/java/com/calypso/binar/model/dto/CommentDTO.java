package com.calypso.binar.model.dto;

import java.sql.Timestamp;

public class CommentDTO {

    private int commentId;
    private String userEmail;
    private String userRole;
    private String comment;
    private Timestamp timestamp;

    public CommentDTO() {
    }

    public CommentDTO(int commentId, String userEmail, String userRole, String comment, Timestamp timestamp) {
        this.commentId = commentId;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "commentId=" + commentId +
                ", userEmail=" + userEmail +
                ", userRole=" + userRole +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
