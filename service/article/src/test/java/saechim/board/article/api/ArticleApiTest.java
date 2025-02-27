package saechim.board.article.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import saechim.board.article.service.response.ArticlePageResponse;
import saechim.board.article.service.response.ArticleResponse;

class ArticleApiTest {
	RestClient restClient = RestClient.create("http://localhost:9000");

	@Test
	void createTest() {
		final ArticleResponse response = create(new ArticleCreateRequest(
			"title", "my content", 1L, 1L
		));

		System.out.println("response = " + response);
	}

	@Test
	void readTest() {
		final ArticleResponse read = read(138111017274159104L);
		System.out.println("read = " + read);
	}

	@Test
	void updateTest() {
		update(138111017274159104L);
		final ArticleResponse read = read(138111017274159104L);
		System.out.println("read = " + read);
	}

	@Test
	void deleteTest() {
		delete(138106671130112000L);
	}

	@Test
	void readAllInfiniteScrollTest() {
		final List<ArticleResponse> articles1 = restClient.get()
			.uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5")
			.retrieve()
			.body(new ParameterizedTypeReference<List<ArticleResponse>>() {
			});

		System.out.println("firstPage");
		for (ArticleResponse articleResponse : articles1) {
			System.out.println("articleResponse.getArticleId() = " + articleResponse.getArticleId());
		}

		final Long lastArticleId = articles1.getLast().getArticleId();
		final List<ArticleResponse> articles2 = restClient.get()
			.uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5&lastArticleId=%s".formatted(lastArticleId))
			.retrieve()
			.body(new ParameterizedTypeReference<List<ArticleResponse>>() {
			});

		System.out.println("secondPage");
		for (ArticleResponse articleResponse : articles2) {
			System.out.println("articleResponse.getArticleId() = " + articleResponse.getArticleId());
		}

	}

	@Test
	void readAllTest() {
		final ArticlePageResponse response = restClient.get()
			.uri("/v1/articles?boardId=1&pageSize=30&page=1")
			.retrieve()
			.body(ArticlePageResponse.class);

		System.out.println("response.getArticleCount() = " + response.getArticleCount());
		for (ArticleResponse article : response.getArticles()) {
			System.out.println("articleId = " + article.getArticleId());
		}
	}

	void delete(final Long articleId) {
		restClient.delete()
			.uri("/v1/articles/{articleId}", articleId)
			.retrieve();
	}

	void update(final Long articleId) {
		restClient.put()
			.uri("/v1/articles/{articleId}", articleId)
			.body(new ArticleUpdateRequest("title2", "my content2"));
	}

	ArticleResponse read(final Long articleId) {
		return restClient.get()
			.uri("/v1/articles/{articleId}", articleId)
			.retrieve()
			.body(ArticleResponse.class);
	}

	ArticleResponse create(final ArticleCreateRequest articleCreateRequest) {
		return restClient.post()
			.uri("/v1/articles")
			.body(articleCreateRequest)
			.retrieve()
			.body(ArticleResponse.class);
	}

	@Getter
	@AllArgsConstructor
	static class ArticleCreateRequest {
		private String title;
		private String content;
		private Long writerId;
		private Long boardId;
	}

	@Getter
	@AllArgsConstructor
	static class ArticleUpdateRequest {
		private String title;
		private String content;
	}

	@Test
	void countTest() {
		ArticleResponse response = create(new ArticleCreateRequest("hi", "content", 1L, 2L));

		Long count1 = restClient.get()
			.uri("/v1/articles/boards/{boardId}/count", 2L)
			.retrieve()
			.body(Long.class);

		System.out.println("count1 = " + count1);

		Long count2 = restClient.delete()
			.uri("/v1/articles/{articelId}", 2L)
			.retrieve()
			.body(Long.class);
		System.out.println("count2 = " + count2);
	}
}