package saechim.board.common.outboxmessagerelay;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OutboxEvent {
	private Outbox outbox;

	public static OutboxEvent of(Outbox outbox) {
		OutboxEvent outboxEvent = new OutboxEvent();
		outboxEvent.outbox = outbox;
		return outboxEvent;
	}
}


