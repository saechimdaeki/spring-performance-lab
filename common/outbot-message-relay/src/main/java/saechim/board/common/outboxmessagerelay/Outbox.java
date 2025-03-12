package saechim.board.common.outboxmessagerelay;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import saechim.board.common.event.EventType;

@Table(name = "outbox")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox {

	@Id
	private Long outboxId;

	@Enumerated(EnumType.STRING)
	private EventType eventType;

	private String payload;

	private Long sharKey;

	private LocalDateTime createdAt;

	public static Outbox create(Long outboxId, EventType eventType, String payload, Long sharKey) {
		Outbox outbox = new Outbox();
		outbox.outboxId = outboxId;
		outbox.eventType = eventType;
		outbox.payload = payload;
		outbox.sharKey = sharKey;
		outbox.createdAt = LocalDateTime.now();
		return outbox;
	}
}
