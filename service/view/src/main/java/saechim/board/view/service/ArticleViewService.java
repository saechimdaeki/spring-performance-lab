package saechim.board.view.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import saechim.board.view.repository.ArticleViewCountRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
	private final ArticleViewCountRepository articleViewCountRepository;
	private final ArticleViewCountBackupProcessor articleViewCountBackupProcessor;
	private static final int BACK_UP_BATCH_SIZE = 100;

	public Long increase(Long articleId, Long userId) {
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
