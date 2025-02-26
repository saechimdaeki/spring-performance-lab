package saechim.board.like.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;
import saechim.board.like.service.response.ArticleLikeResponse;

@Slf4j
public class LikeApiTest {
	RestClient restClient = RestClient.create("http://localhost:9002");

	@Test
	void likeAndUnlikeTest() {
		Long articleId = 9999L;
		like(articleId, 1L);
		like(articleId, 2L);
		like(articleId, 3L);

		ArticleLikeResponse read1 = read(articleId, 1L);
		ArticleLikeResponse read2 = read(articleId, 2L);
		ArticleLikeResponse read3 = read(articleId, 3L);

		log.info("read1: {}", read1);
		log.info("read2: {}", read2);
		log.info("read3: {}", read3);

		unlike(articleId, 1L);
		unlike(articleId, 2L);
		unlike(articleId, 3L);

	}

	void like(Long articleId, Long userId) {
		restClient.get()
			.uri("/v1/article-likes/articles/{articleID}/users/{userId}", articleId, userId)
			.retrieve();
	}

	void unlike(Long articleId, Long userId) {
		restClient.delete()
			.uri("/v1/article-likes/articles/{articleID}/users/{userId}", articleId, userId)
			.retrieve();
	}

	ArticleLikeResponse read(Long articleId, Long userId) {
		return restClient.get()
			.uri("/v1/article-likes/articles/{articleID}/users/{userId}", articleId, userId)
			.retrieve()
			.body(ArticleLikeResponse.class);
	}
}
