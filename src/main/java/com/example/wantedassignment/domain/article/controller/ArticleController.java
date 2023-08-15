package com.example.wantedassignment.domain.article.controller;

import com.example.wantedassignment.domain.article.dto.request.CreateArticle;
import com.example.wantedassignment.domain.article.dto.request.UpdateArticle;
import com.example.wantedassignment.domain.article.dto.response.ArticleDetail;
import com.example.wantedassignment.domain.article.dto.response.ArticleSimple;
import com.example.wantedassignment.domain.article.service.ArticleService;
import java.net.URI;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            final ArticleDetail article = articleService.getOne(id);
            return new ResponseEntity<>(article, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(sort = "id", direction = Direction.DESC)
                                        Pageable pageable) {
        try {
            final Page<ArticleSimple> list = articleService.getList(pageable);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
                                 @RequestBody @Valid CreateArticle createArticle) {
        try {
            final Long savedId = articleService.create(createArticle, Objects.requireNonNull(token));
            return ResponseEntity.created(URI.create("/articles" + savedId))
                    .build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
                                    @RequestBody UpdateArticle updateArticle) {
        try {
            articleService.update(id, updateArticle, Objects.requireNonNull(token));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        try {
            articleService.delete(id, Objects.requireNonNull(token));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
