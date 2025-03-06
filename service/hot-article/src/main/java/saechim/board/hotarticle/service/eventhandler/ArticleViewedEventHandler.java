package saechim.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleViewedEventPayload;
import saechim.board.hotarticle.repository.ArticleViewCountRepository;
import saechim.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleViewedEventHandler implements EventHandler<ArticleViewedEventPayload> {

	private final ArticleViewCountRepository articleViewCountRepository;

	@Override
	public void handle(Event<ArticleViewedEventPayload> event) {
		ArticleViewedEventPayload payload = event.getPayload();
		articleViewCountRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getArticleViewCount(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<ArticleViewedEventPayload> event) {
		return EventType.ARTICLE_VIEWED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleViewedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
