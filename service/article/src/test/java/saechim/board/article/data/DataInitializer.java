package saechim.board.article.data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import saechim.board.article.domain.Article;
import saechim.board.common.snowflake.Snowflake;

@SpringBootTest
public class DataInitializer {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	TransactionTemplate transactionTemplate;

	Snowflake snowflake = new Snowflake();

	CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

	static final int BULK_INSERT_SIZE = 2000;
	static final int EXECUTE_COUNT = 6000;

	@Test
	void initialize() throws InterruptedException {
		final ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (int i = 0; i < EXECUTE_COUNT; i++) {
			executorService.submit(() -> {
				insert();
				latch.countDown();
				System.out.println("latch.getCount() = " + latch.getCount());
			});
		}
		latch.await();
		executorService.shutdown();
	}

	void insert() {
		transactionTemplate.executeWithoutResult(status -> {
			for(int i = 0; i < BULK_INSERT_SIZE; i++) {
				final Article article = Article.create(
					snowflake.nextId(),
					"title" + i,
					"content" + i,
					1L,
					1L
				);
				entityManager.persist(article);
			}
		});
	}
}
