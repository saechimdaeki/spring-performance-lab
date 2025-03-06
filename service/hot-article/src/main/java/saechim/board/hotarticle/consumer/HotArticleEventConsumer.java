package saechim.board.hotarticle.consumer;

import static saechim.board.common.event.EventType.Topic.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventPayload;
import saechim.board.hotarticle.service.HotArticleService;

@Component
@Slf4j
@RequiredArgsConstructor
public class HotArticleEventConsumer {
	private final HotArticleService hotArticleService;

	@KafkaListener(topics = {
		SAECHIM_BOARD_ARTICLE,
		SAECHIM_BOARD_COMMENT,
		SAECHIM_BOARD_LIKE,
		SAECHIM_BOARD_VIEW
	})
	public void listen(String message, Acknowledgment ack) {
		log.info("[HotArticleEventConsumer.listen] message = {}", message);
		Event<EventPayload> event = Event.fromJson(message);
		if (event != null) {
			hotArticleService.handleEvent(event);
		}

		ack.acknowledge();
	}
}
