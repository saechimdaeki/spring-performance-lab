package saechim.board.common.outboxmessagerelay;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventPayload;
import saechim.board.common.event.EventType;
import saechim.board.common.snowflake.Snowflake;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {

	private final Snowflake outboxIdSnowflake = new Snowflake();
	private final Snowflake eventIdSnowflake = new Snowflake();
	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(EventType eventType, EventPayload payload, Long shardKey) {
		Outbox outbox = Outbox.create(
			outboxIdSnowflake.nextId(),
			eventType,
			Event.of(eventIdSnowflake.nextId(), eventType, payload).toJson(),
			shardKey % MessageRelayConstants.SHARD_COUNT
		);

		applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
	}
}
