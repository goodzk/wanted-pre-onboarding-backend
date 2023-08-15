package com.example.wantedassignment.domain.article.dto.request;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateArticle {

    @Size(min = 1, max = 50)
    private String title;

    @Size(min = 1, max = 1000)
    private String body;

    public CreateArticle(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
