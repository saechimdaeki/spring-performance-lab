package saechim.board.articleread.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saechim.board.articleread.service.ArticleReadService;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventPayload;
import saechim.board.common.event.EventType;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleReadEventConsumer {

	private final ArticleReadService articleReadService;

	@KafkaListener(topics = {
		EventType.Topic.SAECHIM_BOARD_ARTICLE,
		EventType.Topic.SAECHIM_BOARD_COMMENT,
		EventType.Topic.SAECHIM_BOARD_LIKE
	})
	public void listen(String message, Acknowledgment ack) {
		log.info("[ArticleReadEventConsumer.read] event {}", message);
		Event<EventPayload> event = Event.fromJson(message);
		if (event != null) {
			articleReadService.handleEvent(event);
		}
		ack.acknowledge();
	}
}
