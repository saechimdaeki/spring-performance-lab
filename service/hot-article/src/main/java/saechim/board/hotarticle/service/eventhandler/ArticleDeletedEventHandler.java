package saechim.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleDeletedEventPayload;
import saechim.board.hotarticle.repository.ArticleCreatedTimeRepository;
import saechim.board.hotarticle.repository.HotArticleListRepository;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {

	private final HotArticleListRepository hotArticleListRepository;
	private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

	@Override
	public void handle(Event<ArticleDeletedEventPayload> event) {
		ArticleDeletedEventPayload payload = event.getPayload();
		articleCreatedTimeRepository.delete(payload.getArticleId());
		hotArticleListRepository.remove(payload.getArticleId(), payload.getCreatedAt());
	}

	@Override
	public boolean supports(Event<ArticleDeletedEventPayload> event) {
		return EventType.ARTICLE_DELETED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleDeletedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
