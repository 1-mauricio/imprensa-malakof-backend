package com.example.blog.repository;

import com.example.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title);

    @Query(value = "SELECT * FROM posts p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.sub_title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))", nativeQuery = true)
    List<Post> findByTitleOrSubTitleOrContent(String searchTerm);
}
