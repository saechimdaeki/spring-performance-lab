package saechim.board.articleread.service.eventHandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.articleread.repository.ArticleQueryModelRepository;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.CommentDeletedEventPayload;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<CommentDeletedEventPayload> event) {
		articleQueryModelRepository.read(event.getPayload().getArticleId())
			.ifPresent(articleQueryModel -> {
				articleQueryModel.updateBy(event.getPayload());
				articleQueryModelRepository.update(articleQueryModel);
			});
	}

	@Override
	public boolean supports(Event<CommentDeletedEventPayload> event) {
		return EventType.COMMENT_DELETED == event.getType();
	}
}
