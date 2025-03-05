package saechim.board.common.event;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import saechim.board.common.event.payload.ArticleCreatedEventPayload;

@Slf4j
class EventTest {

	@Test
	void serde() {
		// given
		ArticleCreatedEventPayload payload = ArticleCreatedEventPayload.builder()
			.articleId(1L)
			.title("title")
			.content("content")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.boardId(1L)
			.writerId(1L)
			.boardArticleCount(23L)
			.build();

		Event<EventPayload> event = Event.of(
			1234L,
			EventType.ARTICLE_CREATED,
			payload
		);

		// when
		String json = event.toJson();
		log.info("json: {}", json);

		Event<EventPayload> result = Event.fronJson(json);

		assertThat(result).isNotNull();
		assertThat(result.getEventId()).isEqualTo(event.getEventId());
		assertThat(result.getType()).isEqualTo(event.getType());
		assertThat(result.getPayload()).isInstanceOf(payload.getClass());

		ArticleCreatedEventPayload resultPayload = (ArticleCreatedEventPayload)result.getPayload();
		assertThat(resultPayload.getArticleId()).isEqualTo(payload.getArticleId());
		assertThat(resultPayload.getTitle()).isEqualTo(payload.getTitle());

	}
}