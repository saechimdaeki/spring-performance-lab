package saechim.board.view.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleViewedEventPayload;
import saechim.board.common.outboxmessagerelay.OutboxEventPublisher;
import saechim.board.view.entity.ArticleViewCount;
import saechim.board.view.repository.ArticleViewCountBackUpRepository;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackupProcessor {
	private final ArticleViewCountBackUpRepository articleViewCountBackUpRepository;
	private final OutboxEventPublisher outboxEventPublisher;

	@Transactional
	public void backUp(Long articleId, Long viewCount) {
		int result = articleViewCountBackUpRepository.updateViewCount(articleId, viewCount);

		if (result == 0) {
			articleViewCountBackUpRepository.findById(articleId)
				.ifPresentOrElse(ignored -> {
					},
					() -> articleViewCountBackUpRepository.save(
						ArticleViewCount.init(articleId, viewCount)
					)
				);
		}

		outboxEventPublisher.publish(
			EventType.ARTICLE_VIEWED,
			ArticleViewedEventPayload.builder()
				.articleId(articleId)
				.articleViewCount(viewCount)
				.build(),
			articleId
		);
	}
}
