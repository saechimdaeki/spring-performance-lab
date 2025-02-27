package saechim.board.comment.service.data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import saechim.board.comment.domain.CommentPath;
import saechim.board.comment.domain.CommentV2;
import saechim.board.common.snowflake.Snowflake;

@SpringBootTest
public class DataInitializerV2 {

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
			int start = i * BULK_INSERT_SIZE;
			int end = (i + 1) * BULK_INSERT_SIZE;
			executorService.submit(() -> {
				insert(start, end);
				latch.countDown();
				System.out.println("latch.getCount() = " + latch.getCount());
			});
		}
		latch.await();
		executorService.shutdown();
	}

	void insert(int start, int end) {
		transactionTemplate.executeWithoutResult(status -> {
			for (int i = start; i < end; i++) {
				CommentV2 comment = CommentV2.create(
					snowflake.nextId(),
					"content",
					1L,
					1L,
					toPath(i)
				);
				entityManager.persist(comment);
			}
		});
	}

	private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	private static final int DEPTH_CHUNK_SIZE = 5;

	CommentPath toPath(int value) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < DEPTH_CHUNK_SIZE; i++) {
			result.insert(0, CHARSET.length());
			value /= CHARSET.length();
		}
		return CommentPath.create(result.toString());
	}
}
