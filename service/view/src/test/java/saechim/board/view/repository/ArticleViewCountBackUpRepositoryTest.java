package saechim.board.view.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import saechim.board.view.entity.ArticleViewCount;

@SpringBootTest
class ArticleViewCountBackUpRepositoryTest {

	@Autowired
	ArticleViewCountBackUpRepository articleViewCountBackUpRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Test
	@Transactional
	void updateViewCountTest() {
		// given
		ArticleViewCount articleViewCount = articleViewCountBackUpRepository
			.save(
				ArticleViewCount.init(1L, 0L)
			);
		entityManager.flush();
		entityManager.clear();

		// when

		int result1 = articleViewCountBackUpRepository.updateViewCount(1L, 100L);
		int result2 = articleViewCountBackUpRepository.updateViewCount(1L, 300L);
		int result3 = articleViewCountBackUpRepository.updateViewCount(1L, 200L);
		
		// then
		assertThat(result1).isEqualTo(1);
		assertThat(result2).isEqualTo(1);
		assertThat(result3).isEqualTo(0);
	}
}