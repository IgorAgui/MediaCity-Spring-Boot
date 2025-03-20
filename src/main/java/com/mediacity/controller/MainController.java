package com.mediacity.controller;

import com.mediacity.model.*;
import com.mediacity.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/home")
public class MainController {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private static final String UPLOAD_DIR = "uploads/";

    public MainController(PostService postService, CommentService commentService, LikeService likeService) {
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @GetMapping
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        model.addAttribute("user", user);
        model.addAttribute("post", new Post());
        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("likeService", likeService);
        model.addAttribute("comment" , commentService);
        return "home";
    }

    @PostMapping("/post")
    public String createPost(
            @RequestParam("description") String description,
            @RequestParam("photo") MultipartFile file,
            HttpSession session) throws IOException {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        String fileName = saveFile(file);

        Post post = new Post();
        post.setDescription(description);
        post.setPhoto(fileName);
        post.setCreationDate(LocalDateTime.now());
        post.setUser(user);

        postService.savePost(post);
        return "redirect:/home";
    }

    @PostMapping("/comment/{postId}")
    public String addComment(
            @PathVariable Long postId,
            @RequestParam String text,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        Post post = postService.getPostById(postId);

        Comment comment = new Comment();
        comment.setText(text);
        comment.setTimestamp(LocalDateTime.now());
        comment.setUser(user);
        comment.setPost(post);

        commentService.saveComment(comment);
        return "redirect:/home";
    }

    @PostMapping("/like/{postId}")
    public String toggleLike(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Post post = postService.getPostById(postId);

        Like existingLike = likeService.findByUserAndPost(user, post);
        if (existingLike != null) {
            likeService.deleteLike(existingLike);
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeService.saveLike(like);
        }
        return "redirect:/home";
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) return "";

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    @GetMapping("/post/{postId}")
    public String getPostModal(@PathVariable Long postId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user"); // Obtém o usuário logado
        Post post = postService.getPostByIdWithCommentsAndUsers(postId);

        model.addAttribute("user", user); // Adiciona o usuário ao model
        model.addAttribute("post", post);

        return "post-modal :: modalContent";
    }


    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        postService.deletePost(postId, user);
        return "redirect:/home";
    }

    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        commentService.deleteComment(commentId, user);
        return "redirect:/home";
    }

}