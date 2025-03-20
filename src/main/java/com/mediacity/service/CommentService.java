package com.mediacity.service;

import com.mediacity.model.Comment;
import com.mediacity.model.Post;
import com.mediacity.model.User;
import com.mediacity.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Ação não permitida");
        }

        commentRepository.delete(comment);
    }

}