package com.nemanja02.projekat.services;

import com.nemanja02.projekat.entities.Comment;
import com.nemanja02.projekat.repositories.comment.CommentRepository;
import javax.inject.Inject;

import java.util.List;

public class CommentService {
    public CommentService() {
        System.out.println(this);
    }

    @Inject
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return this.commentRepository.addComment(comment);
    }
    public List<Comment> getCommentsByArticleID(Integer article_id) {
        return this.commentRepository.getCommentsByArticleID(article_id);
    }
}
