package saechim.board.common.outboxmessagerelay;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageRelay {
	private final OutboxRepository outboxRepository;

	private final MessageRelayCoordinator messageRelayCoordinator;

	private final KafkaTemplate<String, String> messageRelayKafkaTemplate;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void createOutbox(OutboxEvent outboxEvent) {
		log.info("[messageRelay] Created outbox: {}", outboxEvent);
		outboxRepository.save(outboxEvent.getOutbox());
	}

	@Async(value = "messageRelayPublishEventExecutor")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishEvent(OutboxEvent outboxEvent) {
		publishEvent(outboxEvent.getOutbox());
	}

	private void publishEvent(Outbox outbox) {
		try {
			messageRelayKafkaTemplate.send(
				outbox.getEventType().getTopic(),
				String.valueOf(outbox.getSharKey()),
				outbox.getPayload()
			).get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("[messageRelay] Error publishing outbox: {}", outbox, e);
		}
		outboxRepository.delete(outbox);
	}

	@Scheduled(
		fixedDelay = 10,
		initialDelay = 5,
		timeUnit = TimeUnit.SECONDS,
		scheduler = "messageRelayPublishPendingEventExecutor"
	)
	public void publishPendingEvent() {
		AssignedShard assignedShard = messageRelayCoordinator.assigned();
		log.info("[messageRelay] Assigned shard Size: {}", assignedShard.getShards().size());

		for (Long shard : assignedShard.getShards()) {
			List<Outbox> outboxes = outboxRepository.findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
				shard, LocalDateTime.now().minusSeconds(10),
				Pageable.ofSize(100));

			for (Outbox outbox : outboxes) {
				publishEvent(outbox);
			}
		}
	}
}
