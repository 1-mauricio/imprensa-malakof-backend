package com.example.blog.controller;

import com.example.blog.dtos.PostAnalyticsDto;
import com.example.blog.model.Post;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Post savedPost = postService.createPost(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            return new ResponseEntity<>(post.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/title")
    public ResponseEntity<Post> getPostByTitle(@RequestParam String title) {
        Optional<Post> post = postService.getPostByTitle(title);
        if (post.isPresent()) {
            return new ResponseEntity<>(post.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String searchTerm) {
        List<Post> posts = postService.searchPosts(searchTerm);
        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return postService.getPostById(id)
                .map(existingPost -> {
                    if (updates.containsKey("title")) {
                        existingPost.setTitle((String) updates.get("title"));
                    }

                    if (updates.containsKey("subTitle")) {
                        existingPost.setSubTitle((String) updates.get("subTitle"));
                    }

                    if (updates.containsKey("category")) {
                        existingPost.setCategory((String) updates.get("category"));
                    }

                    if (updates.containsKey("content")) {
                        existingPost.setContent((String) updates.get("content"));
                    }

                    return new ResponseEntity<>(postService.updatePost(existingPost), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return postService.getPostById(id)
                .map(post -> {
                    postService.deletePost(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
