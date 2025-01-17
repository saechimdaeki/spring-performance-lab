package saechim.board.article.api;


import org.junit.jupiter.api.Test;
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
			.uri("/v1/articles/{articleId}",articleId)
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
}