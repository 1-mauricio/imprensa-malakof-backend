package com.example.blog.service;

import com.example.blog.dtos.PostAnalyticsDto;
import com.example.blog.model.Post;
import com.example.blog.model.PostView;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.PostViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostViewRepository postViewRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public void incrementPostView(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));

        PostView postView = new PostView();
        postView.setPost(post);
        postViewRepository.save(postView);

        Long viewCount = postViewRepository.countViewsByPostId(postId);

        post.setViewCount(viewCount);
    }

    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            Long viewCount = postViewRepository.countViewsByPostId(post.getId());
            Long viewsThisWeek = postViewRepository.countViewsForPostInLastWeek(post.getId());
            Long viewsThisMonth = postViewRepository.countViewsForPostInLastMonth(post.getId());
            post.setViewCount(viewCount);
            post.setViewsThisWeek(viewsThisWeek);
            post.setViewsThisMonth(viewsThisMonth);
        }

        return posts;
    }

    public Optional<Post> getPostById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            incrementPostView(id);
            Post post = postOptional.get();

            return Optional.of(post);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Post> getPostByTitle(String title) {
        Optional<Post> postOptional = postRepository.findByTitle(title);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            incrementPostView(post.getId());

            return Optional.of(post);
        } else {
            return Optional.empty();
        }
    }


    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<PostAnalyticsDto> getMostViewedPostsThisWeek() {
        return postViewRepository.findMostViewedPostsThisWeek()
                .stream()
                .map(result -> new PostAnalyticsDto((Long) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    public List<PostAnalyticsDto> getMostViewedPostsThisMonth() {
        return postViewRepository.findMostViewedPostsThisMonth()
                .stream()
                .map(result -> new PostAnalyticsDto((Long) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }
}
