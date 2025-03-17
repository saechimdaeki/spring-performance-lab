package saechim.board.articleread.service.eventHandler;

import saechim.board.common.event.Event;
import saechim.board.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
	void handle(Event<T> event);

	boolean supports(Event<T> event);
}
