package saechim.board.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saechim.board.common.event.payload.ArticleCreatedEventPayload;
import saechim.board.common.event.payload.ArticleDeletedEventPayload;
import saechim.board.common.event.payload.ArticleLikeEventPayload;
import saechim.board.common.event.payload.ArticleUnLikeEventPayload;
import saechim.board.common.event.payload.ArticleUpdatedEventPayload;
import saechim.board.common.event.payload.ArticleViewedEventPayload;
import saechim.board.common.event.payload.CommentCreatedEventPayload;
import saechim.board.common.event.payload.CommentDeletedEventPayload;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {

	ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.SAECHIM_BOARD_ARTICLE),
	ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, Topic.SAECHIM_BOARD_ARTICLE),
	ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.SAECHIM_BOARD_ARTICLE),
	COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.SAECHIM_BOARD_COMMENT),
	COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.SAECHIM_BOARD_COMMENT),
	ARTICLE_LIKED(ArticleLikeEventPayload.class, Topic.SAECHIM_BOARD_LIKE),
	ARTICLE_UNLIKED(ArticleUnLikeEventPayload.class, Topic.SAECHIM_BOARD_LIKE),
	ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.SAECHIM_BOARD_VIEW),
	;

	private final Class<? extends EventPayload> payloadClass;
	private final String topic;

	public static EventType from(String type) {
		try {
			return valueOf(type);
		} catch (Exception e) {
			log.error("[eventType].from type = {}", type, e);
			return null;
		}
	}

	public static class Topic {
		public static final String SAECHIM_BOARD_ARTICLE = "saechim-board-article";
		public static final String SAECHIM_BOARD_COMMENT = "saechim-board-comment";
		public static final String SAECHIM_BOARD_LIKE = "saechim-board-like";
		public static final String SAECHIM_BOARD_VIEW = "saechim-board-view";

	}
}
