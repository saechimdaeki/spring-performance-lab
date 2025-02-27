package saechim.board.view.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import saechim.board.view.repository.ArticleViewCountRepository;
import saechim.board.view.repository.ArticleViewDistributedLockRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
	private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
	private final ArticleViewCountRepository articleViewCountRepository;
	private final ArticleViewCountBackupProcessor articleViewCountBackupProcessor;
	private static final int BACK_UP_BATCH_SIZE = 100;
	private static final Duration TTL = Duration.ofMinutes(10);

	public Long increase(Long articleId, Long userId) {
		if (!articleViewDistributedLockRepository.lock(articleId, userId, TTL)) {
			return articleViewCountRepository.read(articleId);
		}
		Long count = articleViewCountRepository.increase(articleId);
		if (count % BACK_UP_BATCH_SIZE == 0) {
			articleViewCountBackupProcessor.backUp(articleId, userId);
		}
		return count;
	}

	public Long count(Long articleId) {
		return articleViewCountRepository.read(articleId);
	}
}
