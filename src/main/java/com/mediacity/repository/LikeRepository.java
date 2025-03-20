package com.mediacity.repository;

import com.mediacity.model.Like;
import com.mediacity.model.Post;
import com.mediacity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndPost(User user, Post post);
    Long countByPost(Post post);
}