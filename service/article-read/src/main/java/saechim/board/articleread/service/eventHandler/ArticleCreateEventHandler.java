package saechim.board.articleread.service.eventHandler;

import java.time.Duration;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.articleread.repository.ArticleQueryModel;
import saechim.board.articleread.repository.ArticleQueryModelRepository;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.ArticleCreatedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleCreateEventHandler implements EventHandler<ArticleCreatedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<ArticleCreatedEventPayload> event) {
		ArticleCreatedEventPayload payload = event.getPayload();
		articleQueryModelRepository.create(
			ArticleQueryModel.create(payload),
			Duration.ofDays(1)
		);
	}

	@Override
	public boolean supports(Event<ArticleCreatedEventPayload> event) {
		return EventType.ARTICLE_CREATED == event.getType();
	}
}
