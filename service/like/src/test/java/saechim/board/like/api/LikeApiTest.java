package saechim.board.like.api;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		like(articleId, 1L, "pessimistic-lock-1");
		like(articleId, 2L, "pessimistic-lock-1");
		like(articleId, 3L, "pessimistic-lock-1");

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

	void like(Long articleId, Long userId, String lockType) {
		restClient.get()
			.uri("/v1/article-likes/articles/{articleID}/users/{userId}/{lockType}", articleId, userId, lockType)
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

	@Test
	void likePerformanceTest() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		likePerformanceTest(executorService, 1111L, "pessimistic-lock-1");
		likePerformanceTest(executorService, 2222L, "pessimistic-lock-2");
		likePerformanceTest(executorService, 3333L, "optimistic-lock");

	}

	void likePerformanceTest(ExecutorService executorService, Long articleId, String lockType) throws
		InterruptedException {
		CountDownLatch latch = new CountDownLatch(3000);
		log.info("lockType {} start", lockType);

		like(articleId, 1L, lockType);

		long start = System.nanoTime();
		for (int i = 0; i < 3000; i++) {
			long userId = i + 2;
			executorService.submit(() -> {
				like(articleId, userId, lockType);
				latch.countDown();
			});
		}

		latch.await();

		long end = System.nanoTime();

		log.info("lockType {} end, time = {} ms", lockType, (end - start) / 1000000);

		Long count = restClient.get()
			.uri("/v1/article-likes/articles/{articleId}", articleId)
			.retrieve()
			.body(Long.class);

		log.info("count: {}", count);
	}
}
