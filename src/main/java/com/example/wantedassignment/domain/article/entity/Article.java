package com.example.wantedassignment.domain.article.entity;

import com.example.wantedassignment.domain.BaseEntity;
import com.example.wantedassignment.domain.article.dto.request.UpdateArticle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private String username;

    @Builder
    public Article(Long id, String title, String body, Long userId, String username) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.username = username;
        this.userId = userId;
    }

    public void update(UpdateArticle updateArticle) {
        this.title = updateArticle.getTitle();
        this.body = updateArticle.getBody();
    }
}
