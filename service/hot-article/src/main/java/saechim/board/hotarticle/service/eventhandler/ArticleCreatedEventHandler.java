package saechim.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleCreatedEventPayload;
import saechim.board.hotarticle.repository.ArticleCreatedTimeRepository;
import saechim.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {
	private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

	@Override
	public void handle(Event<ArticleCreatedEventPayload> event) {

		ArticleCreatedEventPayload payload = event.getPayload();
		articleCreatedTimeRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getCreatedAt(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<ArticleCreatedEventPayload> event) {
		return EventType.ARTICLE_CREATED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleCreatedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
