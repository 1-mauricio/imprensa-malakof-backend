package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customLink")
    public ResponseEntity<Post> getPostByCustomLink(@RequestParam String customLink) {
        Optional<Post> post = postService.getPostByCustomLink(customLink);
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        postService.incrementPostLikes(id); 
        return ResponseEntity.ok().build(); 
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<Void> unlikePost(@PathVariable Long id) {
        postService.decrementPostLikes(id); 
        return ResponseEntity.ok().build(); 
    }


    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Post> existingPostOptional = postService.getPostById(id);

        if (existingPostOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Post existingPost = existingPostOptional.get();

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    existingPost.setTitle((String) value);
                    break;
                case "subTitle":
                    existingPost.setSubTitle((String) value);
                    break;
                case "category":
                    existingPost.setCategory((String) value);
                    break;
                case "content":
                    existingPost.setContent((String) value);
                    break;
                case "imageUrl":
                    existingPost.setImageUrl((String) value);
                    break;
                case "customLink":
                    existingPost.setCustomLink((String) value);
                    break;
                case "readTime":
                    existingPost.setReadTime((Integer) value);
                    break;
                default:
                    break;
            }
        });

        Post updatedPost = postService.updatePost(existingPost);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);

        if (post.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
