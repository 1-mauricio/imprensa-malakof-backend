package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.PostView;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.PostViewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostViewRepository postViewRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public void incrementPostView(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));

        PostView postView = new PostView();
        postView.setPost(post);
        postViewRepository.save(postView);
    }

    public List<Post> getAllPosts() {

        List<Object[]> results = postRepository.findAllWithViewCounts();

        List<Post> posts = new ArrayList<>();

        for (Object[] result : results) {

            Post post = mapResultToPost(result);

            posts.add(post);
        }

        return posts;
    }

    private Post mapResultToPost(Object[] result) {
        Post post = new Post();
        post.setId((Long) result[0]);
        post.setTitle((String) result[6]);
        post.setContent((String) result[2]);

        if (result[3] instanceof java.sql.Timestamp) {
            post.setDate(((java.sql.Timestamp) result[3]).toLocalDateTime());
        }

        post.setReadTime((Integer) result[4]);
        post.setSubTitle((String) result[5]);
        post.setCategory((String) result[1]);
        post.setImageUrl((String) result[8]);
        post.setVersion((Integer) result[7]);
        post.setCustomLink((String) result[9]);
        post.setLikes((Long) result[10]);

        post.setViewsThisWeek((Long) result[13]);
        post.setViewsThisMonth((Long) result[12]);
        post.setViewCount((Long) result[11]);

        return post;
    }

    public Optional<Post> getPostById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            incrementPostView(id);

            Post post = postOptional.get();

            List<Post> posts = List.of(post);
            List<Long> postIds = List.of(post.getId());
            List<Object[]> viewCountsList = postViewRepository.findViewCountsByPostIds(postIds);

            System.out.println("entrou");
            applyViewCountsToPosts(posts, viewCountsList);
            System.out.println("saiu");

            return Optional.of(post);
        }
        return Optional.empty();
    }

    public Optional<Post> getPostByCustomLink(String customLink) {
        Optional<Post> postOptional = postRepository.findByCustomLink(customLink);

        if (postOptional.isPresent()) {
            incrementPostView(postOptional.get().getId());

            Post post = postOptional.get();

            List<Post> posts = List.of(post);
            List<Long> postIds = List.of(post.getId());
            List<Object[]> viewCountsList = postViewRepository.findViewCountsByPostIds(postIds);

            System.out.println("entrou");
            applyViewCountsToPosts(posts, viewCountsList);
            System.out.println("saiu");

            return Optional.of(post);
        }
        return Optional.empty();
    }

    private void applyViewCountsToPosts(List<Post> posts, List<Object[]> viewCounts) {
        for (Object[] result : viewCounts) {
            Long postId = (Long) result[0];
            Long totalViews = (Long) result[1];
            Long viewsThisWeek = (Long) result[2];
            Long viewsThisMonth = (Long) result[3];

            posts.stream()
                    .filter(post -> post.getId().equals(postId))
                    .findFirst()
                    .ifPresent(post -> {
                        post.setViewCount(totalViews);
                        post.setViewsThisWeek(viewsThisWeek);
                        post.setViewsThisMonth(viewsThisMonth);
                    });
        }
    }

    @Transactional
    public void incrementPostLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));

        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void decrementPostLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));

        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
            postRepository.save(post);
        }
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

}

// Previous GetAll in case the performance is a issue
/*
 * public List<Post> getAllPosts() {
 * System.out.println("entrou");
 * 
 * System.out.println("findAll");
 * List<Post> posts = postRepository.findAllByOrderByDateDesc();
 * System.out.println("end - findAll");
 * 
 * System.out.println("list");
 * List<Long> postIds =
 * posts.stream().map(Post::getId).collect(Collectors.toList());
 * System.out.println("end - list");
 * 
 * System.out.println("views");
 * // Obtem contagens de visualizações em lote.
 * List<Object[]> viewCounts =
 * postViewRepository.findViewCountsByPostIds(postIds);
 * System.out.println("end - views");
 * 
 * System.out.println("apply views");
 * // Atualiza os posts com as contagens de visualizações.
 * applyViewCountsToPosts(posts, viewCounts);
 * System.out.println("end - apply views");
 * 
 * System.out.println("saiu");
 * 
 * return posts;
 * }
 */