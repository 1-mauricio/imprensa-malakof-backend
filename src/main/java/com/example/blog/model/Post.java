package com.example.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Getter @Setter private Long id;

    @Column(length = 255, nullable = false)
    @Getter @Setter private String title;

    @Column(length = 255, nullable = true)
    @Getter @Setter private String subTitle;

    @Column(length = 255, nullable = true)
    @Getter @Setter private String category;

    @Column(name = "date", nullable = false)
    @Getter @Setter private LocalDateTime date;

    @Column(name = "readTime", nullable = false)
    @Getter @Setter private Integer readTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Getter @Setter private String content;

    @Version
    @Getter @Setter private Integer version;

    @Column(length = 255, nullable = true)
    @Getter @Setter private String imageUrl;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostView> views;

    @Transient
    @Getter @Setter private Long viewCount;

    @Transient
    @Getter @Setter private Long viewsThisWeek;

    @Transient
    @Getter @Setter private Long viewsThisMonth;

    @Column(length = 255, unique = true)
    @Getter @Setter private String customLink;

    @Column(name = "likes", nullable = false)
    @Getter @Setter private Long likes = 0L;

    public Post() {}

    public Post(String title, String subTitle, String category, LocalDateTime date, Integer readTime, String content, String imageUrl, String customLink) {
        this.title = title;
        this.subTitle = subTitle;
        this.category = category;
        this.date = date;
        this.readTime = readTime;
        this.content = content;
        this.imageUrl = imageUrl;
        this.customLink = customLink;
    }

    @PrePersist
    protected void onCreate() {
        if (date == null) {
            date = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", readTime=" + readTime +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", likes=" + likes +
                '}';
    }
}
