package saechim.board.articleread.service.eventHandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.articleread.repository.ArticleQueryModelRepository;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleUpdatedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleUpdateEventHandler implements EventHandler<ArticleUpdatedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<ArticleUpdatedEventPayload> event) {
		articleQueryModelRepository.read(event.getPayload().getArticleId())
			.ifPresent(articleQueryModel -> {
				articleQueryModel.updateBy(event.getPayload());
				articleQueryModelRepository.update(articleQueryModel);
			});
	}

	@Override
	public boolean supports(Event<ArticleUpdatedEventPayload> event) {
		return EventType.ARTICLE_UPDATED == event.getType();
	}
}
