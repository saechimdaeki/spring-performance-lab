package saechim.board.article.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import saechim.board.article.domain.Article;

@SpringBootTest
@Slf4j
class ArticleRepositoryTest {

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	void findAllTest() {
		final List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);
		log.info("articles.size = {}" , articles.size());
		for (Article article : articles) {
			log.info("article = {}" , article);
		}
		assertThat(articles).isNotEmpty();
		assertThat(articles.size()).isEqualTo(30);
	}

	@Test
	void countTest() {
		final Long count = articleRepository.count(1L, 10000L);
		log.info("count = {}", count);
	}
}