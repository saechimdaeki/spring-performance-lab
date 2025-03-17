package saechim.board.articleread.service.eventHandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.articleread.repository.ArticleQueryModelRepository;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleDeletedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<ArticleDeletedEventPayload> event) {
		ArticleDeletedEventPayload payload = event.getPayload();
		articleQueryModelRepository.delete(payload.getArticleId());
	}

	@Override
	public boolean supports(Event<ArticleDeletedEventPayload> event) {
		return EventType.ARTICLE_DELETED == event.getType();
	}
}
