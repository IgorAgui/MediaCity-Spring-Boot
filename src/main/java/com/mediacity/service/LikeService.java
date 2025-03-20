package com.mediacity.service;

import com.mediacity.model.Like;
import com.mediacity.model.Post;
import com.mediacity.model.User;
import com.mediacity.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    @Autowired // Adicione esta anotação se não estiver usando injeção via construtor
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like findByUserAndPost(User user, Post post) {
        return likeRepository.findByUserAndPost(user, post);
    }

    public void saveLike(Like like) {
        likeRepository.save(like);
    }

    public void deleteLike(Like like) {
        likeRepository.delete(like);
    }

    public Long countLikes(Post post) {
        return likeRepository.countByPost(post);
    }
}