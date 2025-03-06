package saechim.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.CommentCreatedEventPayload;
import saechim.board.hotarticle.repository.ArticleCommentCountRepository;
import saechim.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload> {

	private final ArticleCommentCountRepository articleCommentCountRepository;

	@Override
	public void handle(Event<CommentCreatedEventPayload> event) {
		CommentCreatedEventPayload payload = event.getPayload();
		articleCommentCountRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getArticleCommentCount(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<CommentCreatedEventPayload> event) {
		return EventType.COMMENT_CREATED == event.getType();
	}

	@Override
	public Long findArticleId(Event<CommentCreatedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
