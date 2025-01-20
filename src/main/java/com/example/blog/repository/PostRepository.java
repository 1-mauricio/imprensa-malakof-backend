package com.example.blog.repository;

import com.example.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p.*, " +
            "(SELECT COUNT(*) FROM posts_views pv WHERE pv.post_id = p.id) AS viewCount, " +
            "(SELECT COUNT(*) FROM posts_views pv WHERE pv.post_id = p.id AND pv.view_date >= CURRENT_DATE - INTERVAL '7 days') AS viewsThisWeek, "
            +
            "(SELECT COUNT(*) FROM posts_views pv WHERE pv.post_id = p.id AND pv.view_date >= CURRENT_DATE - INTERVAL '1 month') AS viewsThisMonth "
            +
            "FROM posts p ORDER BY p.date DESC", nativeQuery = true)
    List<Object[]> findAllWithViewCounts();

    List<Post> findAllByOrderByDateDesc();

    Optional<Post> findByTitle(String title);

    Optional<Post> findById(long id);

    @Query(value = "SELECT * FROM posts p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.sub_title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))", nativeQuery = true)
    List<Post> findByTitleOrSubTitleOrContent(String searchTerm);

    Optional<Post> findByCustomLink(String customLink);
}
