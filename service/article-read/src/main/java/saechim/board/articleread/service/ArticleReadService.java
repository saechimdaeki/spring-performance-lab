package saechim.board.articleread.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saechim.board.articleread.client.ArticleClient;
import saechim.board.articleread.client.CommentClient;
import saechim.board.articleread.client.LikeClient;
import saechim.board.articleread.client.ViewClient;
import saechim.board.articleread.repository.ArticleQueryModel;
import saechim.board.articleread.repository.ArticleQueryModelRepository;
import saechim.board.articleread.service.eventHandler.EventHandler;
import saechim.board.articleread.service.response.ArticleReadResponse;
import saechim.board.common.event.Event;
import saechim.board.common.event.EventPayload;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleReadService {
	private final ArticleClient articleClient;
	private final CommentClient commentClient;
	private final ViewClient viewClient;
	private final LikeClient likeClient;
	private final ArticleQueryModelRepository articleQueryModelRepository;
	private final List<EventHandler> eventHandlers;

	public void handleEvent(Event<EventPayload> event) {
		for (EventHandler eventHandler : eventHandlers) {
			if (eventHandler.supports(event)) {
				eventHandler.handle(event);
			}
		}
	}

	public ArticleReadResponse read(Long articleId) {
		ArticleQueryModel articleQueryModel = articleQueryModelRepository.read(articleId)
			.or(() -> fetch(articleId))
			.orElseThrow();
		return ArticleReadResponse.from(
			articleQueryModel,
			viewClient.count(articleId)
		);
	}

	private Optional<ArticleQueryModel> fetch(Long articleId) {
		Optional<ArticleQueryModel> articleQueryModelOptional = articleClient.read(articleId)
			.map(article -> ArticleQueryModel.create(
				article,
				commentClient.count(articleId),
				likeClient.count(articleId)
			));
		articleQueryModelOptional.ifPresent(articleQueryModel -> {
			articleQueryModelRepository.create(articleQueryModel, Duration.ofDays(1));
		});

		log.info("[ArticleReadService.fetch] fetch data, articleId={}, isPresent={}", articleId,
			articleQueryModelOptional.isPresent());
		return articleQueryModelOptional;
	}
}
