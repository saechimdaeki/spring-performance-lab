package saechim.board.articleread.service.eventHandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.articleread.repository.ArticleQueryModelRepository;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.CommentCreatedEventPayload;

@Component
@RequiredArgsConstructor
public class CommentCreateEventHandler implements EventHandler<CommentCreatedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<CommentCreatedEventPayload> event) {
		articleQueryModelRepository.read(event.getPayload().getArticleId())
			.ifPresent(articleQueryModel -> {
				articleQueryModel.updateBy(event.getPayload());
				articleQueryModelRepository.update(articleQueryModel);
			});
	}

	@Override
	public boolean supports(Event<CommentCreatedEventPayload> event) {
		return EventType.COMMENT_CREATED == event.getType();
	}
}
