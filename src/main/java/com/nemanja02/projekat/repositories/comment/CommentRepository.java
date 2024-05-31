package com.nemanja02.projekat.repositories.comment;

import com.nemanja02.projekat.entities.Comment;

import java.util.List;

public interface CommentRepository {
    public Comment addComment(Comment comment);
    public List<Comment> getCommentsByArticleID(Integer article_id);
}
