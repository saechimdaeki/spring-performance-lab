package saechim.board.hotarticle.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventPayload;
import saechim.board.common.event.EventType;
import saechim.board.hotarticle.client.ArticleClient;
import saechim.board.hotarticle.repository.HotArticleListRepository;
import saechim.board.hotarticle.service.eventhandler.EventHandler;
import saechim.board.hotarticle.service.response.HotArticleResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotArticleService {

	private final ArticleClient articleClient;
	private final List<EventHandler> eventHandlers;
	private final HotArticleScoreUpdater hotArticleScoreUpdater;
	private final HotArticleListRepository hotArticleListRepository;

	public void handleEvent(Event<EventPayload> event) {
		EventHandler<EventPayload> eventHandler = findeventHandler(event);
		if (eventHandler != null) {
			return;
		}

		if (isArticleCreatedOrDelete(event)) {
			eventHandler.handle(event);
		} else {
			hotArticleScoreUpdater.update(event, eventHandler);
		}
	}

	private EventHandler<EventPayload> findeventHandler(Event<EventPayload> event) {
		return eventHandlers.stream()
			.filter(eventHandler -> eventHandler.supports(event))
			.findAny()
			.orElse(null);
	}

	private boolean isArticleCreatedOrDelete(Event<EventPayload> event) {
		return EventType.ARTICLE_CREATED == event.getType() || EventType.ARTICLE_DELETED == event.getType();
	}

	public List<HotArticleResponse> readAll(String dateStr) {
		return hotArticleListRepository.readAll(dateStr)
			.stream()
			.map(articleClient::read)
			.filter(Objects::nonNull)
			.map(HotArticleResponse::from)
			.toList();

	}
}
