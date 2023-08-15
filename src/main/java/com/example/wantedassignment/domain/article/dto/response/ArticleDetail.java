package com.example.wantedassignment.domain.article.dto.response;


import com.example.wantedassignment.domain.article.entity.Article;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;

@Data
public class ArticleDetail {

    private String title;

    private String body;

    private String createdDate;

    @Builder
    public ArticleDetail(final Article entity) {
        this.title = entity.getTitle();
        this.body = entity.getBody();
        this.createdDate = entity.getCreateDate()
                .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));
    }
}
