package com.example.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = true)
    private String subTitle;

    @Column(length = 255, nullable = true)
    private String category;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "readTime", nullable = false)
    private Integer readTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Version
    private Integer version;

    // Construtores
    public Post() {}

    public Post(String title, String subTitle, String category, LocalDateTime date, Integer readTime, String content) {
        this.title = title;
        this.subTitle = subTitle;
        this.category = category;
        this.date = date;
        this.readTime = readTime;
        this.content = content;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getReadTime() {
        return readTime;
    }

    public void setReadTime(Integer readTime) {
        this.readTime = readTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // Método chamado antes de persistir para atribuir data atual
    @PrePersist
    protected void onCreate() {
        if (date == null) {
            date = LocalDateTime.now();
        }
    }

    // Método toString
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
                '}';
    }
}
