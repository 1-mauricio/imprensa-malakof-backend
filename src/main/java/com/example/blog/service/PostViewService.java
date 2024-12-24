package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.PostView;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.PostViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostViewService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostViewRepository postViewRepository;

    public void registerView(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        PostView postView = new PostView();
        postView.setPost(post);

        postViewRepository.save(postView);
    }
}