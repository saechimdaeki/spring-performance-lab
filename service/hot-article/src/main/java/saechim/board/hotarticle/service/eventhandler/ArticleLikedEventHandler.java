package saechim.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleLikeEventPayload;
import saechim.board.hotarticle.repository.ArticleLikeCountRepository;
import saechim.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleLikedEventHandler implements EventHandler<ArticleLikeEventPayload> {

	private final ArticleLikeCountRepository articleLikeCountRepository;

	@Override
	public void handle(Event<ArticleLikeEventPayload> event) {
		ArticleLikeEventPayload payload = event.getPayload();
		articleLikeCountRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getArticleLikeCount(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<ArticleLikeEventPayload> event) {
		return EventType.ARTICLE_LIKED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleLikeEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
