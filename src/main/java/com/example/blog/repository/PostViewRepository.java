package com.example.blog.repository;


import com.example.blog.model.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostViewRepository extends JpaRepository<PostView, Long> {
    @Query(value = "SELECT v.post_id, COUNT(v.id) AS view_count " +
            "FROM posts_views v " +
            "WHERE v.view_date >= CURRENT_DATE - 7 " +
            "GROUP BY v.post_id " +
            "ORDER BY view_count DESC", nativeQuery = true)
    List<Object[]> findMostViewedPostsThisWeek();
    @Query(value = "SELECT v.post_id, COUNT(v.id) AS view_count " +
            "FROM posts_views v " +
            "WHERE v.view_date >= CURRENT_DATE - 30 " +
            "GROUP BY v.post_id " +
            "ORDER BY view_count DESC", nativeQuery = true)
    List<Object[]> findMostViewedPostsThisMonth();

    @Query(value = "SELECT COUNT(v.id) FROM posts_views v WHERE v.post_id = :postId", nativeQuery = true)
    Long countViewsByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT COUNT(v.id) FROM posts_views v WHERE v.post_id = :postId AND v.view_date >= CURRENT_DATE - 7", nativeQuery = true)
    Long countViewsForPostInLastWeek(Long postId);

    @Query(value = "SELECT COUNT(v.id) FROM posts_views v WHERE v.post_id = :postId AND v.view_date >= CURRENT_DATE - 30", nativeQuery = true)
    Long countViewsForPostInLastMonth(Long postId);
}
