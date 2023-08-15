package com.example.wantedassignment.domain.article.service;

import com.example.wantedassignment.common.util.JwtProvider;
import com.example.wantedassignment.domain.article.dto.request.CreateArticle;
import com.example.wantedassignment.domain.article.dto.request.UpdateArticle;
import com.example.wantedassignment.domain.article.dto.response.ArticleDetail;
import com.example.wantedassignment.domain.article.dto.response.ArticleSimple;
import com.example.wantedassignment.domain.article.entity.Article;
import com.example.wantedassignment.domain.article.repository.ArticleRepository;
import com.example.wantedassignment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserService userService;

    private final JwtProvider jwtProvider;


    public ArticleDetail getOne(final Long id) {
        final Article article = articleRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
                );
        return new ArticleDetail(article);
    }

    public Page<ArticleSimple> getList(final Pageable pageable) {
        final Page<Article> articles = articleRepository.findAll(pageable);
        return articles.map(ArticleSimple::new);
    }

    @Transactional
    public Long create(final CreateArticle createArticle, final String token) {
        final Long userId = getUserIdInToken(token);

        return articleRepository.save(Article.builder()
                .body(createArticle.getBody())
                .title(createArticle.getTitle())
                        .userId(userId)
                        .username(userService.getUsernameById(userId))
                .build()).getId();
    }

    @Transactional
    public void update(final Long articleId, final UpdateArticle updateArticle, final String token) {
        final Long userId = getUserIdInToken(token);
        final Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        if (!userId.equals(article.getUserId())) {
            throw new RuntimeException("게시글을 수정할 수 있는 사용자는 게시글 작성자만이어야 합니다.");
        }
        article.update(updateArticle);
    }

    @Transactional
    public void delete(final Long articleId, final String token) {
        final Long userId = getUserIdInToken(token);
        final Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!userId.equals(article.getUserId())) {
            throw new RuntimeException("게시글을 수정할 수 있는 사용자는 게시글 작성자만이어야 합니다.");
        }
        articleRepository.delete(article);
    }

    private Long getUserIdInToken(final String token) {
        return Long.valueOf(jwtProvider.getUserId(token.replace("Bearer", "")));
    }
}
