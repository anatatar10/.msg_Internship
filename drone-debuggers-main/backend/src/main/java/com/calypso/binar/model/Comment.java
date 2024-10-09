package com.calypso.binar.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "comment_text", nullable = false, length = 1024)
    private String commentText;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    // Default constructor
    public Comment() {}

    // Constructor with parameters
    public Comment(Case caseEntity, User user, String commentText, Timestamp timestamp) {
        this.caseEntity = caseEntity;
        this.user = user;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public Case getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(Case caseEntity) {
        this.caseEntity = caseEntity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    // toString method
    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", caseEntity=" + caseEntity.getSystemCaseId() +
                ", user=" + user +
                ", commentText='" + commentText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
