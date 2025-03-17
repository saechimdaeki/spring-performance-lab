package saechim.board.articleread.service.eventHandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.articleread.repository.ArticleQueryModelRepository;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleUnLikeEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleUnLikedEventHandler implements EventHandler<ArticleUnLikeEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<ArticleUnLikeEventPayload> event) {
		articleQueryModelRepository.read(event.getPayload().getArticleId())
			.ifPresent(articleQueryModel -> {
				articleQueryModel.updateBy(event.getPayload());
				articleQueryModelRepository.update(articleQueryModel);
			});
	}

	@Override
	public boolean supports(Event<ArticleUnLikeEventPayload> event) {
		return EventType.ARTICLE_UNLIKED == event.getType();
	}
}
