package com.example.blog.repository;

import com.example.blog.model.PostView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

        /**
         * Obtém as contagens de visualizações (total, semanais e mensais) para uma
         * lista de posts.
         *
         * @param postIds Lista de IDs dos posts.
         * @return Lista de arrays contendo postId, totalViews, viewsThisWeek e
         *         viewsThisMonth.
         */
        @Query(value = "SELECT " +
                        "  v.post_id AS postId, " +
                        "  COUNT(v) AS totalViews, " +
                        "  SUM(CASE WHEN v.view_date >= CURRENT_DATE - 7 THEN 1 ELSE 0 END) AS viewsThisWeek, " +
                        "  SUM(CASE WHEN v.view_date >= CURRENT_DATE - 30 THEN 1 ELSE 0 END) AS viewsThisMonth " +
                        "FROM posts_views v " +
                        "WHERE v.post_id IN :postIds " +
                        "GROUP BY v.post_id", nativeQuery = true)
        List<Object[]> findViewCountsByPostIds(@Param("postIds") List<Long> postIds);

}
