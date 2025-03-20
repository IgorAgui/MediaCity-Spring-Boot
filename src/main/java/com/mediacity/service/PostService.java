package com.mediacity.service;

import com.mediacity.model.Comment;
import com.mediacity.model.Post;
import com.mediacity.model.User;
import com.mediacity.repository.CommentRepository;
import com.mediacity.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // Injeção de dependência atualizada
    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreationDateDesc();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
    }

    public void deletePost(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Ação não permitida");
        }
        postRepository.delete(post);
    }

    public Post getPostByIdWithCommentsAndUsers(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        // Carrega comentários com usuários
        post.setComments(commentRepository.findCommentsWithUsersByPostId(postId));

        return post;
    }
}