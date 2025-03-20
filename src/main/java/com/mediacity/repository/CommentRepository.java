package com.mediacity.repository;

import com.mediacity.model.Comment;
import com.mediacity.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.post.id = :postId ORDER BY c.timestamp ASC")
    List<Comment> findCommentsWithUsersByPostId(@Param("postId") Long postId);
}