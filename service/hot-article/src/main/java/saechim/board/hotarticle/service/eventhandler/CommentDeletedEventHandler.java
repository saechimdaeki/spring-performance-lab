package saechim.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.CommentDeletedEventPayload;
import saechim.board.hotarticle.repository.ArticleCommentCountRepository;
import saechim.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload> {

	private final ArticleCommentCountRepository articleCommentCountRepository;

	@Override
	public void handle(Event<CommentDeletedEventPayload> event) {
		CommentDeletedEventPayload payload = event.getPayload();
		articleCommentCountRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getArticleCommentCount(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<CommentDeletedEventPayload> event) {
		return EventType.COMMENT_DELETED == event.getType();
	}

	@Override
	public Long findArticleId(Event<CommentDeletedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
