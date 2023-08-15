package com.example.wantedassignment.domain.article.service;

import static org.assertj.core.api.Assertions.*;

import com.example.wantedassignment.common.util.JwtProvider;
import com.example.wantedassignment.domain.article.dto.request.CreateArticle;
import com.example.wantedassignment.domain.article.dto.request.UpdateArticle;
import com.example.wantedassignment.domain.article.dto.response.ArticleDetail;
import com.example.wantedassignment.domain.article.dto.response.ArticleSimple;
import com.example.wantedassignment.domain.article.repository.ArticleRepository;
import com.example.wantedassignment.domain.user.dto.request.JoinUser;
import com.example.wantedassignment.domain.user.repository.UserRepository;
import com.example.wantedassignment.domain.user.service.UserService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    private String accessToken;

    private Long savedArticleId;

    @BeforeEach
    void setUp() {
        JoinUser joinUser = new JoinUser(
                "test@gmail.com", "12345678", "이름"
        );
        final Long userId = userService.join(joinUser).getMessage();
        accessToken = jwtProvider.generateAccessToken(userId);

        CreateArticle createArticle = new CreateArticle("제목제목", "본문 내용임");
        savedArticleId = articleService.create(createArticle, accessToken);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @Test
    void 특정_게시글_조회() {
        // when
        final ArticleDetail article = articleService.getOne(savedArticleId);

        // then
        assertThat(article.getTitle()).isEqualTo("제목제목");
    }

    @Test
    void 게시글_목록_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "id");

        // when
        final List<ArticleSimple> articles = articleService.getList(pageable).getContent();
        final Long articleId1 = articles.get(0).getId();

        // then
        assertThat(articleId1).isEqualTo(savedArticleId);
    }

    @Test
    void 특정_게시글_수정() {
        // given
        UpdateArticle updateArticle = new UpdateArticle("new title", "body");

        // when
        articleService.update(savedArticleId, updateArticle, accessToken);

        // then
        final ArticleDetail article = articleService.getOne(savedArticleId);
        assertThat(article.getTitle()).isEqualTo("new title");
        assertThat(article.getBody()).isEqualTo("body");
    }

    @Test
    void 특정_게시글_삭제() {
        // when
        articleService.delete(savedArticleId, accessToken);

        // then
        assertThatThrownBy(() -> articleService.getOne(savedArticleId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 새로운_게시글_생성() {
        // given
        CreateArticle createArticle = new CreateArticle("title111", "body body 11");

        // when
        final Long articleId = articleService.create(createArticle, accessToken);

        // then
        assertThat(articleId).isGreaterThan(1L);
    }
}