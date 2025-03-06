package saechim.board.hotarticle.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventPayload;
import saechim.board.hotarticle.repository.ArticleCreatedTimeRepository;
import saechim.board.hotarticle.repository.HotArticleListRepository;
import saechim.board.hotarticle.service.eventhandler.EventHandler;

@Component
@RequiredArgsConstructor
public class HotArticleScoreUpdater {

	private final HotArticleListRepository hotArticleListRepository;
	private final HotArticleScoreCalculator hotArticleScoreCalculator;
	private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

	private static final long HOT_ARTICLE_COUNT = 10;
	private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(10);

	public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
		Long articleId = eventHandler.findArticleId(event);
		LocalDateTime createdTime = articleCreatedTimeRepository.read(articleId);

		if (!isArticleCreatedToday(createdTime))
			return;

		eventHandler.handle(event);

		long score = hotArticleScoreCalculator.calculate(articleId);
		hotArticleListRepository.add(articleId, createdTime, score, HOT_ARTICLE_COUNT, HOT_ARTICLE_TTL);
	}

	private boolean isArticleCreatedToday(LocalDateTime createdTime) {
		return createdTime != null && createdTime.toLocalDate().equals(LocalDate.now());
	}
}
