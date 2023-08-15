package com.example.wantedassignment.domain.article.dto.request;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateArticle {

    @Size(min = 1, max = 50)
    private String title;

    @Size(min = 1, max = 1000)
    private String body;

    public UpdateArticle(String newTitle, String body) {
        title = newTitle;
        this.body = body;
    }
}
