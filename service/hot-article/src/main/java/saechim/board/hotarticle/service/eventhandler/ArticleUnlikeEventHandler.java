package saechim.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleUnLikeEventPayload;
import saechim.board.hotarticle.repository.ArticleLikeCountRepository;
import saechim.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleUnlikeEventHandler implements EventHandler<ArticleUnLikeEventPayload> {

	private final ArticleLikeCountRepository articleLikeCountRepository;

	@Override
	public void handle(Event<ArticleUnLikeEventPayload> event) {
		ArticleUnLikeEventPayload payload = event.getPayload();
		articleLikeCountRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getArticleLikeCount(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<ArticleUnLikeEventPayload> event) {
		return EventType.ARTICLE_UNLIKED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleUnLikeEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
